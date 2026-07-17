import sys
import os
import json
import time
import threading
import argparse
import logging
import socket
import datetime
import platform

from flask import Flask, request, jsonify

from PyQt5.QtWidgets import (
    QApplication, QMainWindow, QWidget, QGridLayout,
    QLabel, QVBoxLayout, QHBoxLayout, QPushButton,
    QProgressBar, QFrame, QSizePolicy
)
from PyQt5.QtCore import (
    Qt, QPropertyAnimation, QTimer, QObject, pyqtSignal,
    pyqtSlot, QSize
)
from PyQt5.QtGui import (
    QIcon, QPixmap, QPainter, QBrush, QRadialGradient,
    QColor, QPen, QFont
)

from screeninfo import get_monitors
import wmi

# --- Global Constants ---
DEFAULT_API_HOST = '127.0.0.1' # Localhost
DEFAULT_API_PORT = 5002 # Default port for Flask API
DEFAULT_BAY_NAME = "Unloading Bay" # Default bay name
DEFAULT_SCREEN_NAME = "primary" # Default screen name for positioning
DEFAULT_MANUFACTURER_NAME = "ABCD" # Default manufacturer name
DEFAULT_MONITOR_PID_NO = "2222" # Default monitor PID number
DEFAULT_MONITOR_SERIAL_NO = "xyz" # Default monitor serial number
ROWS = 2 # Number of rows in the grid
COLS = 3 # Number of columns in the grid
REFRESH_INTERVAL = 1000 # Refresh interval in milliseconds
VALID_METER_IDS = set(range(1, (ROWS * COLS) + 1)) # Valid meter IDs from 1 to ROWS*COLS
DEBUG_MODE = False # Set to True for loading sample data, False for production mode
PASS_BG_COLOR = "#e6ffe6" # Light green background for PASS status
PASS_BORDER_COLOR = "#2ecc71" # Green border for PASS status
FAIL_BG_COLOR = "#ffe6e6" # Light red background for FAIL status
FAIL_BORDER_COLOR = "#e74c3c" # Red border for FAIL status
TEXT_COLOR = "#2c3e50" # Default text color for labels
N_A_BG_COLOR = "#f0f0f0" # Light gray background for N/A status
N_A_BORDER_COLOR = "#cccccc" # Gray border for N/A status

# --- Logging Setup ---
# Global logger instance
logger = None

def setup_logging():
    """Sets up application-wide logging to a timestamped file within a daily subfolder."""
    
    # Get today's date
    today_date = datetime.datetime.now().strftime("%Y-%m-%d")
    
    # Construct the daily log directory path
    log_dir = os.path.join("logs", today_date)
    
    # Create the directory if it doesn't exist
    if not os.path.exists(log_dir):
        try:
            os.makedirs(log_dir)
        except OSError as e:
            # Fallback if directory creation fails (e.g., permission issues)
            # Log to console and use the root 'logs' folder or current directory
            print(f"ERROR: Could not create log directory {log_dir}: {e}. Logging to 'logs/' or current directory.")
            log_dir = "logs" # Fallback to top-level logs folder
            if not os.path.exists(log_dir):
                os.makedirs(log_dir) # Try to create top-level if it also failed earlier

    timestamp = datetime.datetime.now().strftime("%Y%m%d_%H%M%S")
    log_filename = os.path.join(log_dir, f"app_{timestamp}.log")

    global logger
    if logger: # Prevent adding handlers multiple times if setup_logging is called more than once
        for handler in logger.handlers[:]:
            logger.removeHandler(handler)
            handler.close()
    
    logger = logging.getLogger('MeterDisplayApp')
    logger.setLevel(logging.DEBUG) # Set to INFO for less verbose logging in production

    # Create file handler which logs even debug messages
    fh = logging.FileHandler(log_filename)
    fh.setLevel(logging.DEBUG)

    # Create console handler with a higher level to display only warnings/errors to console
    ch = logging.StreamHandler()
    ch.setLevel(logging.INFO) # Only show INFO and above in console

    # Create formatter and add it to the handlers
    formatter = logging.Formatter('%(asctime)s - %(name)s - %(levelname)s - %(message)s')
    fh.setFormatter(formatter)
    ch.setFormatter(formatter)

    # Add the handlers to the logger
    logger.addHandler(fh)
    logger.addHandler(ch)

    logger.info(f"Logging initialized. Log file: {log_filename}")
    logger.info(f"Application started on {platform.system()} {platform.release()} ({platform.version()})")
    logger.info(f"Python version: {sys.version}")

# --- MeterStatusUpdater (unchanged) ---
class MeterStatusUpdater(QObject):
    data_updated = pyqtSignal(dict)

# --- MeterStatusServer ---
class MeterStatusServer:
    def __init__(self, initial_port, bay_name):
        self.app = Flask(__name__)
        self.port = initial_port
        self.bay_name = bay_name
        self.status_data = []
        self.pallet_number = f"Waiting for Pallet" # Initial state
        self.update_signal = None

        if DEBUG_MODE:
            self.status_data = self._generate_sample_data()
            self.pallet_number = "DEBUG-PALLET-1234" # Overridden for debug mode
            logger.debug("MeterStatusServer initialized in DEBUG_MODE with sample data.")
        else:
            logger.info("MeterStatusServer initialized in production mode. Starting with idle state.")
            self.status_data = self._generate_placeholder_data() # Ensure initial data structure
            self.pallet_number = f"Waiting for Pallet"


        self.app.add_url_rule('/api/meters', 'get_all_meters', self.get_all_meters, methods=['GET'])
        self.app.add_url_rule('/api/meters/<int:meter_id>', 'get_meter', self.get_meter, methods=['GET'])
        self.app.add_url_rule('/api/meters/<int:meter_id>', 'update_meter', self.update_meter, methods=['PUT'])
        self.app.add_url_rule('/api/meters/batch', 'update_all_meters', self.update_all_meters, methods=['POST'])
        self.app.add_url_rule('/api/pallet', 'update_pallet', self.update_pallet, methods=['PUT'])
        self.app.add_url_rule('/api/idle', 'set_idle_state', self.set_idle_state, methods=['GET']) # Changed to GET

        logger.info(f"Flask API server configured on {DEFAULT_API_HOST}:{self.port}")

    def _generate_placeholder_data(self):
        logger.debug("Generating placeholder data for meters.")
        placeholders = []
        for i in range(ROWS * COLS):
            meter_id = i + 1
            placeholders.append({
                'id': meter_id,
                'serialNo': "N/A",
                'status': "N/A",
                'reason': "Waiting for data",
                'position': f"{meter_id}",
                'timestamp': 'Not updated yet'
            })
        return placeholders

    def _generate_sample_data(self):
        logger.debug("Generating sample data for meters (DEBUG_MODE).")
        sample_data = []
        current_time = datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S")
        temp_sample_meters = {m['id']: m for m in self._generate_placeholder_data()}
        temp_sample_meters[1].update({
            'serialNo': "SN-UNL-001",
            'status': "PASS",
            'reason': "No defects found during unloading inspection.",
            'timestamp': current_time
        })
        temp_sample_meters[2].update({
            'serialNo': "SN-UNL-002",
            'status': "FAIL",
            'reason': "Minor scratch detected on casing. Requires further review.",
            'timestamp': current_time
        })
        temp_sample_meters[3].update({
            'serialNo': "SN-UNL-003",
            'status': "REVIEWED",
            'reason': "Manual check completed. Passed after minor adjustment.",
            'timestamp': current_time
        })
        temp_sample_meters[4].update({
            'serialNo': "SN-UNL-004",
            'status': "FAIL",
            'reason': "Err_RON\nErr_ROFF",
            'timestamp': current_time
        })
        temp_sample_meters[5].update({
            'serialNo': "SN-UNL-005",
            'status': "FAIL",
            'reason': "Component misalignment. Requires repair or disposal.",
            'timestamp': current_time
        })
        sample_data = sorted(list(temp_sample_meters.values()), key=lambda x: x['id'])
        return sample_data

    def get_all_meters(self):
        logger.debug("API: get_all_meters called.")
        return jsonify({
            'meters': self.status_data,
            'pallet_number': self.pallet_number
        })

    def get_meter(self, meter_id):
        logger.debug(f"API: get_meter called for ID: {meter_id}.")
        meter = next((m for m in self.status_data if m['id'] == meter_id), None)
        if meter:
            return jsonify(meter)
        logger.warning(f"API: Meter ID {meter_id} not found.")
        return jsonify({'error': 'Meter not found'}), 404

    def update_meter(self, meter_id):
        data = request.get_json()
        logger.info(f"API: update_meter called for ID: {meter_id} with data: {data}.")
        if meter_id not in VALID_METER_IDS:
            logger.error(f"API Error: Received update for invalid meter ID: {meter_id}. Must be in {VALID_METER_IDS}")
            return jsonify({
                                'error': f'Invalid meter ID: {meter_id}. Must be one of {list(VALID_METER_IDS)}. Only 1-{ROWS*COLS} positions are valid.'}), 400
        if not self.status_data:
            self.status_data = self._generate_placeholder_data()
            logger.info("Initialized status_data with placeholders during first update.")

        if 'pallet_number' in data:
            self.pallet_number = data['pallet_number']
            logger.info(f"Pallet number updated via single meter update: {self.pallet_number}")
        elif self.pallet_number == f"{self.bay_name} - Waiting for Pallet":
            self.pallet_number = "Current Pallet" # Generic pallet if not provided
            logger.info("Pallet number set to 'Current Pallet' as it was in initial waiting state.")

        meter = next((m for m in self.status_data if m['id'] == meter_id), None)
        valid_statuses = ['PASS', 'FAIL', 'DISPOSED', 'REVIEWED', 'N/A', ' ', '', 'WFR']
        if 'status' not in data or data['status'] not in valid_statuses:
            logger.error(f"API Error: Invalid status '{data.get('status')}' for meter {meter_id}. Must be one of {valid_statuses}.")
            return jsonify({'error': f'Invalid status. Must be one of {valid_statuses}.'}), 400
        
        if meter:
            old_status = meter['status']
            meter['status'] = data['status']
            meter['timestamp'] = data.get('timestamp', datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S"))
            if 'reason' in data:
                meter['reason'] = data['reason']
            if 'serialNo' in data:
                meter['serialNo'] = data['serialNo']
            logger.info(f"Meter {meter_id} updated from '{old_status}' to '{meter['status']}'.")
        else:
            new_meter = {
                'id': meter_id,
                'serialNo': data.get('serialNo', "N/A"),
                'status': data['status'],
                'reason': data.get('reason', 'N/A'),
                'position': f"{meter_id}",
                'timestamp': data.get('timestamp', datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S"))
            }
            self.status_data.append(new_meter)
            self.status_data.sort(key=lambda x: x['id'])
            meter = new_meter
            logger.info(f"New meter {meter_id} added with status '{meter['status']}'.")

        if self.update_signal:
            self.update_signal.emit({
                'type': 'meters',
                'data': self.status_data
            })
            logger.debug(f"Emitted 'meters' update signal for meter {meter_id}.")
        return jsonify(meter)

    def update_all_meters(self):
        data = request.get_json()
        logger.info(f"API: update_all_meters called with data. Pallet: {data.get('pallet_number')}, Meters: {len(data.get('meters', []))}")
        
        if 'pallet_number' in data:
            self.pallet_number = data['pallet_number']
            logger.info(f"Pallet number updated via batch update: {self.pallet_number}")
        else:
            if self.pallet_number == f"{self.bay_name} - Waiting for Pallet":
                self.pallet_number = "Current Pallet"
                logger.info("Pallet number set to 'Current Pallet' during batch update as it was in initial waiting state.")

        if 'meters' not in data or not isinstance(data['meters'], list):
            logger.error("API Error: Invalid data format for batch update. Expected a list of meters under 'meters' key.")
            return jsonify({'error': 'Invalid data format. Expected a list of meters under "meters" key.'}), 400
        
        temp_meters_data = {m['id']: m for m in self._generate_placeholder_data()}
        valid_statuses = ['PASS', 'FAIL', 'DISPOSED', 'REVIEWED', 'N/A', ' ', '', 'WFR']
        
        for meter_update in data['meters']:
            meter_id = meter_update.get('id')
            if meter_id is None or meter_id not in VALID_METER_IDS:
                logger.warning(f"API Warning: Skipping invalid or out-of-range meter ID in batch update: {meter_id}. Must be in {list(VALID_METER_IDS)}")
                continue
            current_meter = temp_meters_data[meter_id]
            current_meter['serialNo'] = meter_update.get('serialNo', current_meter['serialNo'])
            current_status = meter_update.get('status')
            current_meter['status'] = current_status if current_status in valid_statuses else 'N/A'
            current_meter['reason'] = meter_update.get('reason', current_meter['reason'])
            current_meter['position'] = meter_update.get('position', current_meter['position'])
            current_meter['timestamp'] = meter_update.get('timestamp', datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S"))
            logger.debug(f"Batch update: Meter {meter_id} set to status '{current_meter['status']}'.")

        self.status_data = sorted(list(temp_meters_data.values()), key=lambda x: x['id'])
        
        if self.update_signal:
            self.update_signal.emit({
                'type': 'full_update',
                'meters': self.status_data,
                'pallet_number': self.pallet_number
            })
            logger.debug("Emitted 'full_update' signal.")
        return jsonify({
            'message': 'All meters updated successfully',
            'pallet_number': self.pallet_number
        })

    def update_pallet(self):
        data = request.get_json()
        logger.info(f"API: update_pallet called with data: {data}.")
        if 'pallet_number' in data:
            self.pallet_number = data['pallet_number']
            if self.update_signal:
                self.update_signal.emit({
                    'type': 'pallet',
                    'pallet_number': self.pallet_number
                })
                logger.debug(f"Emitted 'pallet' update signal with new pallet: {self.pallet_number}.")
            return jsonify({'message': 'Pallet number updated successfully'})
        logger.error("API Error: Invalid data format for update_pallet. 'pallet_number' key missing.")
        return jsonify({'error': 'Invalid data format'}), 400

    def set_idle_state(self):
        logger.info("API: set_idle_state called. Transitioning to idle mode.")
        self.status_data = []
        self.pallet_number = f"{self.bay_name} - Waiting for Pallet"
        
        if self.update_signal:
            self.update_signal.emit({
                'type': 'idle'
            })
            logger.debug("Emitted 'idle' signal.")
        return jsonify({'message': 'Screen set to idle mode'}), 200

    def run(self):
        try:
            logger.info(f"Starting Flask API server on {DEFAULT_API_HOST}:{self.port}...")
            # Suppress Flask's default verbose logging if desired, or configure it via Python logging
            # self.app.logger.disabled = True
            # logging.getLogger('werkzeug').setLevel(logging.ERROR) # Suppress Werkzeug HTTP access logs
            self.app.run(host=DEFAULT_API_HOST, port=self.port, threaded=True)
        except Exception as e:
            logger.critical(f"Flask API server failed to start: {e}", exc_info=True)
            sys.exit(1)

# --- TestCell (unchanged except for logging) ---
class TestCell(QWidget):
    def __init__(self, meter_data, parent=None):
        super().__init__(parent)
        self.meter_data = meter_data
        self.animation = None
        self.initUI()
        if self.meter_data['status'] == 'FAIL':
            self.setup_animation()
        self.setSizePolicy(QSizePolicy.Expanding, QSizePolicy.Expanding)
        logger.debug(f"TestCell {meter_data['id']} initialized with status: {meter_data['status']}.")

    def _get_stylesheet(self):
        if self.meter_data['status'] == 'PASS':
            return f"""
                background-color: {PASS_BG_COLOR};
                border: 3px solid {PASS_BORDER_COLOR};
                border-radius: 18px;
            """
        elif self.meter_data['status'] == 'FAIL':
            return f"""
                background-color: {FAIL_BG_COLOR};
                border: 3px solid {FAIL_BORDER_COLOR};
                border-radius: 18px;
            """
        else:
            return f"""
                background-color: {N_A_BG_COLOR};
                border: 3px solid {N_A_BORDER_COLOR};
                border-radius: 18px;
            """

    def initUI(self):
        self.setContentsMargins(0, 0, 0, 0)
        self.container = QWidget()
        self.container.setStyleSheet(self._get_stylesheet())
        main_cell_layout = QVBoxLayout(self.container)
        main_cell_layout.setContentsMargins(10, 10, 10, 10)
        main_cell_layout.setSpacing(5)

        top_row_layout = QHBoxLayout()
        top_row_layout.setContentsMargins(0, 0, 0, 0)
        icon_text = "✅" if self.meter_data['status'] == 'PASS' else ("❌" if self.meter_data['status'] == 'FAIL' else "⚪")
        self.icon_label = QLabel(icon_text)
        self.icon_label.setAlignment(Qt.AlignCenter | Qt.AlignVCenter)
        self.icon_label.setStyleSheet("""
            font-size: 40px;
            margin: 0;
            padding: 0;
        """)
        top_row_layout.addWidget(self.icon_label)
        top_row_layout.addStretch(1)
        self.position_frame = QFrame()
        self.position_frame.setFixedSize(60, 60)
        pos_frame_layout = QVBoxLayout(self.position_frame)
        pos_frame_layout.setContentsMargins(0, 0, 0, 0)
        pos_frame_layout.addStretch()
        self.position_label = QLabel(str(self.meter_data['id']))
        self.position_label.setAlignment(Qt.AlignCenter)
        self.position_label.setStyleSheet(f"""
            font-size: 48px;
            font-weight: bold;
            color: {TEXT_COLOR};
        """)
        pos_frame_layout.addWidget(self.position_label)
        pos_frame_layout.addStretch()
        self.position_frame.setStyleSheet(self._get_position_frame_stylesheet())
        top_row_layout.addWidget(self.position_frame)
        main_cell_layout.addLayout(top_row_layout)

        self.serial_label = QLabel(f"S/N: {self.meter_data['serialNo']}")
        self.serial_label.setAlignment(Qt.AlignCenter)
        self.serial_label.setStyleSheet(f"""
            font-size: 32px;
            font-weight: bold;
            color: {TEXT_COLOR};
        """)
        main_cell_layout.addWidget(self.serial_label)

        self.status_label = QLabel(self.meter_data['status'])
        self.status_label.setAlignment(Qt.AlignCenter)
        self.status_label.setStyleSheet(f"""
            font-size: 24px;
            font-weight: bold;
            color: {TEXT_COLOR};
        """)
        main_cell_layout.addWidget(self.status_label)

        self.reason_label = QLabel(f"Reason: {self.meter_data['reason']}")
        self.reason_label.setAlignment(Qt.AlignCenter)
        self.reason_label.setWordWrap(True)
        self.reason_label.setStyleSheet(f"""
            font-size: 20px;
            color: {TEXT_COLOR};
        """)
        main_cell_layout.addWidget(self.reason_label)

        outer_layout = QVBoxLayout(self)
        outer_layout.addWidget(self.container)

    def _get_position_frame_stylesheet(self):
        if self.meter_data['status'] == 'PASS':
            return f"""
                QFrame {{
                    background-color: transparent;
                    border: 2px solid {PASS_BORDER_COLOR};
                    border-radius: 10px;
                }}
                QLabel {{
                    color: {PASS_BORDER_COLOR};
                }}
            """
        elif self.meter_data['status'] == 'FAIL':
            return f"""
                QFrame {{
                    background-color: transparent;
                    border: 2px solid {FAIL_BORDER_COLOR};
                    border-radius: 10px;
                }}
                QLabel {{
                    color: {FAIL_BORDER_COLOR};
                }}
            """
        else:
            return f"""
                QFrame {{
                    background-color: transparent;
                    border: 2px solid {N_A_BORDER_COLOR};
                    border-radius: 10px;
                }}
                QLabel {{
                    color: {TEXT_COLOR};
                }}
            """

    def _get_reason_frame_stylesheet(self):
        if self.meter_data['status'] == 'PASS':
            return f"""
                QFrame {{
                    background-color: {PASS_BG_COLOR};
                    border: 2px solid {PASS_BORDER_COLOR};
                    border-radius: 10px;
                    min-height: 80px;
                }}
            """
        elif self.meter_data['status'] == 'FAIL':
            return f"""
                QFrame {{
                    background-color: {FAIL_BG_COLOR};
                    border: 2px solid {FAIL_BORDER_COLOR};
                    border-radius: 10px;
                    min-height: 80px;
                }}
            """
        else:
            return f"""
                QFrame {{
                    background-color: {N_A_BG_COLOR};
                    border: 2px solid {N_A_BORDER_COLOR};
                    border-radius: 10px;
                    min-height: 80px;
                }}
            """

    def update_status(self, new_data):
        self.meter_data = new_data
        self.serial_label.setText(f"S/N: {new_data['serialNo']}")
        self.status_label.setText(new_data['status'])
        self.reason_label.setText(f"Reason: {new_data['reason']}")
        self.reason_label.setWordWrap(True)
        icon_text = "✅" if new_data['status'] == 'PASS' else ("❌" if new_data['status'] == 'FAIL' else "⚪")
        self.icon_label.setText(icon_text)
        self.position_label.setText(str(new_data['id']))
        self.position_frame.setStyleSheet(self._get_position_frame_stylesheet())
        self.container.setStyleSheet(self._get_stylesheet())
        if new_data['status'] == 'FAIL':
            if not self.animation or self.animation.state() == QPropertyAnimation.Stopped:
                self.setup_animation()
        else:
            if self.animation and self.animation.state() == QPropertyAnimation.Running:
                self.animation.stop()
                self.container.setWindowOpacity(1.0)
        logger.debug(f"TestCell {new_data['id']} updated to status: {new_data['status']}.")

    def setup_animation(self):
        self.animation = QPropertyAnimation(self.container, b"windowOpacity")
        self.animation.setDuration(1500)
        self.animation.setStartValue(0.8)
        self.animation.setEndValue(1.0)
        self.animation.setLoopCount(-1)
        self.animation.start()
        logger.debug(f"Animation started for TestCell {self.meter_data['id']}.")

# --- MeterDisplayApp ---
class MeterDisplayApp(QMainWindow):
    def __init__(self, api_server, bay_name, screen_name_arg, manufacturer_name, monitor_pid_no, monitor_serial_no):
        super().__init__()
        self.is_fullscreen = True
        self.api_server = api_server
        self.bay_name = bay_name
        self.screen_name_arg = screen_name_arg
        self.monitor_manufacturer_name = manufacturer_name
        self.monitor_pid_no = monitor_pid_no
        self.monitor_serial_no = monitor_serial_no
        self.cells = {}
        self.pallet_number = self.api_server.pallet_number # Initial value from server
        self.is_idle_mode = not DEBUG_MODE # Determine initial mode based on DEBUG_MODE

        self.idle_widget = None
        self.normal_geometry = None
        self.initUI()
        self._get_monitor_pid_vid() # For logging/info about monitors

        self.updater = MeterStatusUpdater()
        self.updater.data_updated.connect(self.handle_update)
        self.api_server.update_signal = self.updater.data_updated
        
        self.timer = QTimer()
        self.timer.timeout.connect(self.fetch_data)
        self.timer.start(REFRESH_INTERVAL)
        logger.info(f"MeterDisplayApp initialized. Bay: '{self.bay_name}'.")

    def initUI(self):
        self.setWindowTitle(f"{self.bay_name} - Live Meter Status")
        main_widget = QWidget()
        self.setCentralWidget(main_widget)
        self.main_layout = QVBoxLayout(main_widget)
        self.main_layout.setContentsMargins(10, 10, 10, 10)
        self.main_layout.setSpacing(10)

        # Header Layout
        header_layout = QHBoxLayout()
        header_layout.setSpacing(10)
        header_layout.setContentsMargins(500, 15, 450, 0)
        
        self.bay_name_label = QLabel(self.bay_name)
        self.bay_name_label.setAlignment(Qt.AlignLeft | Qt.AlignVCenter)
        self.bay_name_label.setStyleSheet("""
            font-size: 28px;
            font-weight: bold;
            color: #2c3e50;
            margin-bottom: 15px;
        """)
        header_layout.addWidget(self.bay_name_label)
        header_layout.addStretch(1) # This stretch pushes content to the sides

        self.pallet_label = QLabel(self.pallet_number)
        self.pallet_label.setAlignment(Qt.AlignRight | Qt.AlignVCenter)
        self.pallet_label.setStyleSheet("""
            font-size: 28px;
            font-weight: bold;
            color: #2c3e50;
            margin-bottom: 15px;
        """)
        header_layout.addWidget(self.pallet_label)
        
        self.main_layout.addLayout(header_layout)
        self.main_layout.addSpacing(10)

        # Grid Container (for meter cells)
        self.grid_container = QWidget()
        self.grid_layout = QGridLayout(self.grid_container)
        self.grid_layout.setSpacing(20)
        self.grid_layout.setContentsMargins(0, 0, 0, 0)
        for col in range(COLS):
            self.grid_layout.setColumnStretch(col, 1)
        for row in range(ROWS):
            self.grid_layout.setRowStretch(row, 1)
        self.main_layout.addWidget(self.grid_container) # Add to main layout

        # Idle Widget (created once, visibility toggled)
        self.idle_widget = QWidget()
        idle_layout = QVBoxLayout(self.idle_widget)
        idle_layout.setAlignment(Qt.AlignCenter)
        idle_layout.setSpacing(30)

        # Bay Name Label for Idle Screen (Large and Central)
        self.idle_bay_name_label = QLabel(self.bay_name)
        self.idle_bay_name_label.setAlignment(Qt.AlignCenter)
        self.idle_bay_name_label.setStyleSheet("""
            font-size: 60px;
            font-weight: bold;
            color: #34495e;
            margin-bottom: 10px;
        """)
        idle_layout.addWidget(self.idle_bay_name_label)

        # Pallet Number Label for Idle Screen (Large and Central)
        self.idle_pallet_label = QLabel(self.pallet_number) # Will be updated by _show_idle_screen
        self.idle_pallet_label.setAlignment(Qt.AlignCenter)
        self.idle_pallet_label.setStyleSheet("""
            font-size: 48px;
            font-weight: bold;
            color: #34495e;
            margin-bottom: 20px;
        """)
        idle_layout.addWidget(self.idle_pallet_label)

        loading_bar = QProgressBar()
        loading_bar.setRange(0, 0)
        loading_bar.setTextVisible(False)
        loading_bar.setFixedSize(300, 30)
        loading_bar.setStyleSheet("""
            QProgressBar {
                border: 2px solid #3498db;
                border-radius: 5px;
                background-color: transparent;
            }
            QProgressBar::chunk {
                background-color: #3498db;
                border-radius: 3px;
            }
        """)
        loading_bar_layout = QHBoxLayout()
        loading_bar_layout.addStretch()
        loading_bar_layout.addWidget(loading_bar)
        loading_bar_layout.addStretch()
        idle_layout.addLayout(loading_bar_layout)
        
        self.main_layout.addWidget(self.idle_widget) # Add to main layout

        # Initial visibility based on DEBUG_MODE
        if self.is_idle_mode:
            self._show_idle_screen_elements() # Show idle elements
        else:
            self._hide_idle_screen_elements() # Hide idle elements and show grid
            self.pallet_label.setText(self.api_server.pallet_number) # Update header pallet
            self.update_meter_data(self.api_server.status_data) # Populate grid

        self._set_window_on_screen_smart(
            self.screen_name_arg,
            self.monitor_manufacturer_name,
            self.monitor_pid_no,
            self.monitor_serial_no
        )
        logger.info("UI initialized and window positioned.")

    def _get_consistent_monitor_identification(self):
        logger.debug("Attempting to get consistent monitor identification.")
        monitors_combined_data = []
        screen_monitors = get_monitors()
        screen_info_map = {m.name.replace('\\\\.\\', '').lower(): m for m in screen_monitors}

        if platform.system() == "Windows":
            wmi_monitors_data = self._get_monitor_pid_vid()
            
            for wmi_mon in wmi_monitors_data:
                matched_screen_mon = None
                for s_mon in screen_monitors:
                    if s_mon.name.replace('\\\\.\\', '').lower() == wmi_mon.get('name', '').lower() or \
                       (s_mon.is_primary and wmi_mon.get('is_primary', False)):
                        matched_screen_mon = s_mon
                        break

                if matched_screen_mon:
                    monitors_combined_data.append({
                        'index': matched_screen_mon.id if hasattr(matched_screen_mon, 'id') else len(monitors_combined_data),
                        'name': matched_screen_mon.name,
                        'width': matched_screen_mon.width,
                        'height': matched_screen_mon.height,
                        'x': matched_screen_mon.x,
                        'y': matched_screen_mon.y,
                        'is_primary': matched_screen_mon.is_primary,
                        'manufacturer': wmi_mon.get('manufacturer', 'N/A'),
                        'product_id': wmi_mon.get('product_id', 'N/A'),
                        'serial_number': wmi_mon.get('serial_number', 'N/A'),
                        'consistent_id': f"{wmi_mon.get('manufacturer', '')}_{wmi_mon.get('product_id', '')}"
                    })
                else:
                    monitors_combined_data.append({
                        'index': len(monitors_combined_data),
                        'name': wmi_mon.get('name', 'Unknown Display').replace('\\\\.\\', ''),
                        'width': 0, 'height': 0, 'x': 0, 'y': 0,
                        'is_primary': wmi_mon.get('is_primary', False),
                        'manufacturer': wmi_mon.get('manufacturer', 'N/A'),
                        'product_id': wmi_mon.get('product_id', 'N/A'),
                        'serial_number': wmi_mon.get('serial_number', 'N/A'),
                        'consistent_id': f"{wmi_mon.get('manufacturer', '')}_{wmi_mon.get('product_id', '')}"
                    })
            
            for s_mon in screen_monitors:
                found = False
                for combined_mon in monitors_combined_data:
                    if combined_mon['name'].replace('\\\\.\\', '').lower() == s_mon.name.replace('\\\\.\\', '').lower():
                        found = True
                        break
                if not found:
                    monitors_combined_data.append({
                        'index': s_mon.id if hasattr(s_mon, 'id') else len(monitors_combined_data),
                        'name': s_mon.name,
                        'width': s_mon.width,
                        'height': s_mon.height,
                        'x': s_mon.x,
                        'y': s_mon.y,
                        'is_primary': s_mon.is_primary,
                        'manufacturer': 'N/A',
                        'product_id': 'N/A',
                        'serial_number': 'N/A',
                        'consistent_id': f"ScreeninfoOnly_{s_mon.id if hasattr(s_mon, 'id') else s_mon.name}"
                    })
        else:
            monitors_combined_data = [{
                'index': m.id if hasattr(m, 'id') else i,
                'name': m.name,
                'width': m.width,
                'height': m.height,
                'x': m.x,
                'y': m.y,
                'is_primary': m.is_primary,
                'manufacturer': 'N/A',
                'product_id': 'N/A',
                'serial_number': 'N/A',
                'consistent_id': f"Monitor_{m.id if hasattr(m, 'id') else i}"
            } for i, m in enumerate(screen_monitors)]
            
        logger.debug(f"Consistent monitor identification complete. Found {len(monitors_combined_data)} monitors.")
        return monitors_combined_data

    def _get_monitor_by_pid_vid(self, manufacturer, product_id, serial_number=None):
        logger.debug(f"Searching for monitor by PID/VID: Mfg='{manufacturer}', PID='{product_id}', Serial='{serial_number}'")
        monitors = self._get_consistent_monitor_identification()
        
        for monitor in monitors:
            matches_manufacturer = str(monitor.get('manufacturer', '')).upper().strip() == manufacturer.upper().strip()
            matches_pid = str(monitor.get('product_id', '')).upper().strip() == str(product_id).upper().strip()
            matches_serial = (serial_number is None or serial_number.strip() == "") or \
                             (str(monitor.get('serial_number', '')).strip().upper() == str(serial_number).strip().upper())
            
            if matches_manufacturer and matches_pid and matches_serial:
                logger.info(f"Monitor found by PID/VID: {monitor['name']}")
                return monitor
        
        logger.warning("No monitor found matching provided PID/VID details.")
        return None

    def _get_monitor_pid_vid(self):
        if platform.system() != "Windows":
            logger.info("WMI Monitor ID retrieval is only supported on Windows. Skipping.")
            return []

        try:
            c = wmi.WMI(namespace='wmi')
            monitors_wmi_data = []

            for monitor in c.WmiMonitorID():
                manufacturer = "".join(chr(x) for x in monitor.ManufacturerName if x != 0).strip()
                product_code = "".join(chr(x) for x in monitor.ProductCodeID if x != 0).strip()
                serial_number = "".join(chr(x) for x in monitor.SerialNumberID if x != 0).strip()
                
                monitors_wmi_data.append({
                    'name': '', # Placeholder, will be filled by screeninfo match
                    'manufacturer': manufacturer,
                    'product_id': product_code,
                    'serial_number': serial_number,
                    'week': monitor.WeekOfManufacture,
                    'year': monitor.YearOfManufacture
                })
            logger.debug(f"Successfully retrieved {len(monitors_wmi_data)} monitors via WMI.")
            return monitors_wmi_data
        except wmi.WMIError as e:
            logger.error(f"WMI error encountered during monitor ID retrieval: {e}", exc_info=True)
            return []
        except Exception as e:
            logger.error(f"An unexpected error occurred during WMI monitor ID retrieval: {e}", exc_info=True)
            return []

    def _set_window_on_screen_smart(self, screen_arg, manufacturer, product_id, serial_number):
        logger.info(f"Attempting to set window on screen. screen_arg='{screen_arg}', Mfg='{manufacturer}', PID='{product_id}', Serial='{serial_number}'")
        all_monitors = get_monitors()

        logger.debug("Available Monitors:")
        for i, m in enumerate(all_monitors):
            logger.debug(f"  Monitor {i}: Name='{m.name}', Primary={m.is_primary}, Geometry=({m.x},{m.y},{m.width},{m.height})")

        target_monitor = None

        if screen_arg:
            logger.debug(f"Prioritizing --screen-name '{screen_arg}'.")
            try:
                screen_index = int(screen_arg)
                if 0 <= screen_index < len(all_monitors):
                    target_monitor = all_monitors[screen_index]
                    logger.info(f"Found monitor by index {screen_index}: '{target_monitor.name}'.")
                else:
                    logger.warning(f"Monitor index {screen_index} out of range. Trying by name.")
            except ValueError:
                lower_screen_arg = screen_arg.lower()
                for monitor in all_monitors:
                    normalized_monitor_name = monitor.name.lower()
                    if platform.system() == "Windows" and normalized_monitor_name.startswith('\\\\.\\'):
                        normalized_monitor_name = normalized_monitor_name.replace('\\\\.\\', '')
                    
                    if normalized_monitor_name == lower_screen_arg:
                        target_monitor = monitor
                        logger.info(f"Found monitor by name '{screen_arg}': '{target_monitor.name}'.")
                        break
                if not target_monitor:
                    logger.warning(f"Monitor with name '{screen_arg}' not found.")
        else:
            logger.info("No --screen-name provided. Proceeding to hardware details.")

        if not target_monitor and (manufacturer != DEFAULT_MANUFACTURER_NAME or \
                                   product_id != DEFAULT_MONITOR_PID_NO or \
                                   serial_number != DEFAULT_MONITOR_SERIAL_NO):
            logger.debug("Attempting to find monitor using hardware details.")
            target_monitor_from_hw = self._get_monitor_by_pid_vid(manufacturer, product_id, serial_number)
            if target_monitor_from_hw:
                for s_mon in all_monitors:
                    normalized_s_mon_name = s_mon.name.replace('\\\\.\\', '').lower()
                    if normalized_s_mon_name == target_monitor_from_hw.get('name', '').replace('\\\\.\\', '').lower() or \
                       (s_mon.x == target_monitor_from_hw['x'] and s_mon.y == target_monitor_from_hw['y'] and s_mon.width == target_monitor_from_hw['width'] and s_mon.height == target_monitor_from_hw['height']):
                        target_monitor = s_mon
                        logger.info(f"Mapped hardware match to screeninfo monitor: '{target_monitor.name}'.")
                        break
                if not target_monitor:
                     logger.warning("Could not map hardware details match to an active screeninfo monitor. This might happen if WMI finds an inactive display.")
            else:
                logger.warning("No monitor found matching provided hardware details.")

        if not target_monitor:
            logger.info("Falling back to primary monitor.")
            target_monitor = next((m for m in all_monitors if m.is_primary), None)
            if not target_monitor and all_monitors:
                target_monitor = all_monitors[0]
                logger.info(f"Primary monitor not explicitly found, using first available: '{target_monitor.name}'.")
            elif target_monitor:
                logger.info(f"Using primary monitor: '{target_monitor.name}'.")
            else:
                logger.critical("No monitors detected at all. Cannot set specific screen geometry.")
                self.showFullScreen()
                return

        if target_monitor:
            self.setGeometry(target_monitor.x, target_monitor.y, target_monitor.width, target_monitor.height)
            self.showFullScreen()
            logger.info(f"Window set to fullscreen on monitor: '{target_monitor.name}' at ({target_monitor.x},{target_monitor.y},{target_monitor.width},{target_monitor.height}).")
        else:
            logger.critical("Critical Error: No target monitor could be determined. Showing full screen on current display.")
            self.showFullScreen()

    def fetch_data(self):
        # This timer-based fetch is now less critical as updates come via signal,
        # but can serve as a fallback or periodic refresh.
        # Ensure it doesn't interfere with the signal-based updates.
        #logger.debug("Periodic data fetch triggered.")
        if not self.is_idle_mode:
            # Only update UI elements that might not be covered by explicit signals
            # The main data update is handled by handle_update
            self.pallet_label.setText(self.api_server.pallet_number)
            # self.update_meter_data(self.api_server.status_data) # Avoid redundant calls if signal handles it
        


    @pyqtSlot(dict)
    def handle_update(self, data):
        logger.info(f"Received update signal: type='{data.get('type')}'.")
        if data.get('type') == 'idle':
            self._show_idle_screen_elements()
            logger.info("Transitioned to idle screen.")
        elif data.get('type') in ['full_update', 'meters', 'pallet']:
            self._hide_idle_screen_elements()
            self.pallet_label.setText(self.api_server.pallet_number)
            self.update_meter_data(self.api_server.status_data)
            logger.info("Transitioned to active meter display.")

    def _show_idle_screen_elements(self):
        logger.debug("Showing idle screen elements.")
        self.is_idle_mode = True

        # Hide the header labels when in idle mode
        self.bay_name_label.hide()
        self.pallet_label.hide()

        self.grid_container.hide()
        self.idle_widget.show()

        # Update central idle labels
        self.idle_bay_name_label.setText(self.bay_name)
        self.idle_pallet_label.setText(self.api_server.pallet_number) # Use current pallet number from server

        # Clear existing cells from grid when entering idle
        for i in reversed(range(self.grid_layout.count())):
            widget = self.grid_layout.itemAt(i).widget()
            if widget:
                widget.setParent(None)
                widget.deleteLater()
        self.cells.clear()
        logger.debug("Grid cleared and idle widget shown.")

    def _hide_idle_screen_elements(self):
        logger.debug("Hiding idle screen elements.")
        self.is_idle_mode = False
        self.idle_widget.hide()
        self.grid_container.show()

        # Show the header labels when exiting idle mode
        self.bay_name_label.show()
        self.pallet_label.show()

        logger.debug("Idle widget hidden and grid shown.")

    def update_meter_data(self, meters):
        logger.debug(f"Updating meter data. Number of meters: {len(meters)}.")
        # Clear existing cells
        for i in reversed(range(self.grid_layout.count())):
            widget = self.grid_layout.itemAt(i).widget()
            if widget:
                widget.setParent(None)
                widget.deleteLater()
        self.cells.clear()

        # Create and add new cells
        for meter in meters:
            meter_id = meter['id']
            row = (meter_id - 1) // COLS
            col = (meter_id - 1) % COLS

            if meter_id == 1:
                col = 2  # Meter ID 1 goes to the last column of the first row
            elif meter_id == 3:
                col = 0  # Meter ID 3 goes to the first column of the first row
            
            cell = TestCell(meter)
            self.grid_layout.addWidget(cell, row, col)
            self.cells[meter_id] = cell
        
        self.grid_layout.invalidate()
        logger.debug("Meter grid updated.")

    def keyPressEvent(self, event):
        if event.key() == Qt.Key_Escape or event.key() == Qt.Key_F11:
            self.toggle_fullscreen()
            logger.info("Fullscreen toggled via key press.")

    def toggle_fullscreen(self):
        if self.isFullScreen():
            self.showNormal()
            if hasattr(self, 'normal_geometry') and self.normal_geometry:
                self.setGeometry(self.normal_geometry)
            else:
                self.showMaximized()
            logger.info("Exited fullscreen.")
        else:
            self.normal_geometry = self.geometry()
            self.showFullScreen()
            logger.info("Entered fullscreen.")
        self.is_fullscreen = not self.is_fullscreen

# --- run_application ---
def run_application():
    setup_logging() # Initialize logging at the very beginning

    parser = argparse.ArgumentParser(description="Unified Meter Status Display Application")
    parser.add_argument('--port', type=int, default=DEFAULT_API_PORT,
                        help=f"Port number for the Flask API server (default: {DEFAULT_API_PORT})")
    parser.add_argument('--bay-name', type=str, default=DEFAULT_BAY_NAME,
                        help=f"Name of the bay to display (e.g., 'Unloading Bay', 'Rejection Bay') (default: '{DEFAULT_BAY_NAME}')")
    parser.add_argument('--screen-name', type=str, default=DEFAULT_SCREEN_NAME,
                        help=f"Name or index of the screen to display the app on (e.g., 'primary', 'screen1', '0') (default: '{DEFAULT_SCREEN_NAME}')")
    parser.add_argument('--manufacturer-name', type=str, default=DEFAULT_MANUFACTURER_NAME,
                        help=f"Manufacturer Name screen to display the app on (e.g., 'primary', 'screen1', '0') (default: '{DEFAULT_MANUFACTURER_NAME}')")

    parser.add_argument('--monitor-pid-no', type=str, default=DEFAULT_MONITOR_PID_NO,
                        help=f"Monitor PID Number of the screen to display the app on (e.g., 'primary', 'screen1', '0') (default: '{DEFAULT_MONITOR_PID_NO}')")

    parser.add_argument('--monitor-serial-no', type=str, default=DEFAULT_MONITOR_SERIAL_NO,
                        help=f"Monitor serial number of the screen to display the app on (e.g., 'primary', 'screen1', '0') (default: '{DEFAULT_MONITOR_SERIAL_NO}')")

    args = parser.parse_args()
    logger.info(f"Application arguments parsed: {args}")
    
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    try:
        sock.bind((DEFAULT_API_HOST, args.port))
        sock.close()
        logger.info(f"Port {args.port} is available.")
    except OSError as e:
        if e.errno == 10048:
            logger.critical(f"Error: Port {args.port} is already in use. Please choose a different port or ensure no other instance of the application is running.", exc_info=True)
            sys.exit(1)
        else:
            logger.critical(f"An unexpected error occurred while checking port {args.port}: {e}", exc_info=True)
            sys.exit(1)
    finally:
        sock.close()
        
    api_server = MeterStatusServer(initial_port=args.port, bay_name=args.bay_name)
    server_thread = threading.Thread(target=api_server.run, daemon=True)
    server_thread.start()
    logger.info("Flask API server thread started.")
    
    app = QApplication(sys.argv)
    app.setStyle("Fusion")
    
    time.sleep(1) # Give server a moment to start
    
    window = MeterDisplayApp(api_server, args.bay_name, args.screen_name,
                             args.manufacturer_name, args.monitor_pid_no, args.monitor_serial_no)
    
    try:
        logger.info("Starting PyQt application event loop.")
        sys.exit(app.exec_())
    except Exception as e:
        logger.critical(f"PyQt application crashed: {e}", exc_info=True)
        sys.exit(1)

if __name__ == "__main__":
    run_application()