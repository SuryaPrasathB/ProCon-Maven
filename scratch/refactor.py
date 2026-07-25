import re
import os

def refactor_java(filepath):
    with open(filepath, 'r', encoding='utf-8') as f:
        content = f.read()

    # 1. BayStateEngine
    pattern1 = re.compile(
        r'(\w+Timer)\s*=\s*new\s+Timer\(\);\s*\n\s*(\w+Engine)\s*=\s*new\s+BayStateEngine\(([^,]+),\s*(new\s+[^\(]+\(\))\);\s*\n\s*\1\.schedule\(\2,\s*\d+\);',
        re.MULTILINE
    )
    def repl1(m):
        timer_var = m.group(1)
        engine_var = m.group(2)
        bay_key = m.group(3)
        constructor = m.group(4)
        return f"{engine_var} = new BayStateEngine({bay_key}, {constructor});\n\t\t{timer_var} = com.tasnetwork.calibration.conveyor.dashboard.BayThreadManager.scheduleTask({bay_key}, {engine_var}, \"START\");"

    content, num_subs1 = pattern1.subn(repl1, content)
    print(f"{os.path.basename(filepath)} - Replaced {num_subs1} BayStateEngine schedules.")

    # 2. General Stop/Reset/Bypass (No engine variable)
    # We must identify the Bay Key for each TimerTask. We can match the method name to figure out the bay key!
    # But it's easier to just match the assignment and schedule inside a method.
    # We can use a regex to match the method name and then find all Timers in it.
    
    # Just save the BayStateEngine replacements for now, as that's the main clash issue.
    # Wait, the other tasks (Stop/Reset) also clash!
    
    # Let's fix triggerStartByBayKey dual-execution bug in StateExecutorController:
    if "StateExecutorController" in filepath:
        # We need to remove the dual calls.
        # Find the comment: // Also trigger MainControlPaneController UI updates if instance is available
        dual_exec = re.search(r'// Also trigger MainControlPaneController UI updates if instance is available\s*\n\s*com\.tasnetwork\.calibration\.conveyor\.dashboard\.MainControlPaneController\s+mcpc.*?\}', content, re.DOTALL)
        if dual_exec:
            # We comment it out or remove it? The user said "We updated StateExecutorController as the central base gateway"
            # If we remove it, the UI won't update when triggered from context menu!
            # But the UI polling threads already update the UI based on flags! (e.g., btnFtStop.setDisable(true) happens based on flags).
            # Actually, let's keep it, but we need the BayThreadManager to handle it.
            pass

    with open(filepath, 'w', encoding='utf-8') as f:
        f.write(content)

if __name__ == '__main__':
    refactor_java(r'd:\SURYA\.DEVELOPMENT\.PROJECTS\.CONVEYOR PROJECT\ProCON\ProCon-Maven\src\main\java\com\tasnetwork\calibration\conveyor\dashboard\MainControlPaneController.java')
    refactor_java(r'd:\SURYA\.DEVELOPMENT\.PROJECTS\.CONVEYOR PROJECT\ProCON\ProCon-Maven\src\main\java\com\tasnetwork\calibration\conveyor\StateExecutorController.java')
