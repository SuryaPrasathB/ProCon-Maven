import os
import glob
import re

files_and_flags = {
    "WaitingBayStop.java": "VerificWaiting.setStopProcessCompletedWaitingBay(true);",
    "VerificationTestBayStop.java": "Verification.setStopProcessCompletedVerificBay(true);",
    "UnloadingBayStop.java": "Unloading.setStopProcessCompletedUnloadingBay(true);",
    "STA_NoLoadTestBay2Stop.java": "StaNld_Bay2.setStopProcessCompletedStaNldBay2(true);",
    "STA_NoLoadTestBay1Stop.java": "StaNld_Bay1.setStopProcessCompletedStaNldBay1(true);",
    "RejectionBayStop.java": "Rejection.setStopProcessCompletedRejectionBay(true);",
    "LoadingBayStop.java": "Loading.setStopProcessCompletedLoadingBay(true);",
    "InsulationResistanceTestBayStop.java": "Ir.setStopProcessCompletedIrtBay(true);",
    "FunctionalTestBayStop.java": "Ft.setStopProcessCompletedFtBay(true);",
    "HighVoltageTestBayStop.java": "Hv.setStopProcessCompletedHvtBay(true);",
    "CommunicationTestBayStop.java": "Comm.setStopProcessCompletedCommBay(true);",
    "CalibrationBayStop.java": "Calib.setStopProcessCompletedCalibBay(true);"
}

base_dir = r"d:\SURYA\.DEVELOPMENT\.PROJECTS\.CONVEYOR PROJECT\ProCON\ProCon-Maven\src\main\java\com\tasnetwork\calibration\conveyor\bay"

for root, _, files in os.walk(base_dir):
    for filename in files:
        if filename in files_and_flags:
            flag_code = files_and_flags[filename]
            filepath = os.path.join(root, filename)
            
            with open(filepath, 'r') as f:
                content = f.read()
            
            # Use regex to find `if (nextStateName != null && !nextStateName.isEmpty()) { ... }` blocks
            # that do NOT have an else branch immediately following them, and are followed by a `for` loop.
            pattern = re.compile(r'(\s*if\s*\(nextStateName\s*!=\s*null\s*&&\s*!nextStateName\.isEmpty\(\)\)\s*\{[^{}]*(?:\{[^{}]*\}[^{}]*)*\})(\s*for\s*\()')
            
            replacement = r'\1 else {\n\t\t\t\t\t\t' + flag_code + r'\n\t\t\t\t\t\tbreak;\n\t\t\t\t\t}\2'
            
            new_content = pattern.sub(replacement, content)
            
            if new_content != content:
                print(f"Patched {filename}")
                with open(filepath, 'w') as f:
                    f.write(new_content)
            else:
                print(f"Could not patch {filename}")
