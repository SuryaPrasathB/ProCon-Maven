import os
import re

directory = r"d:\SURYA\.DEVELOPMENT\.PROJECTS\.CONVEYOR PROJECT\ProCON\ProCon-Maven\src\main\java\com\tasnetwork\calibration\conveyor\bay"
pattern = re.compile(r'!nextStateName\.isEmpty\(\)(?!\s*&&\s*!nextStateName\.equals\("Select State"\))')
replacement = r'!nextStateName.isEmpty() && !nextStateName.equals("Select State")'

for root, _, files in os.walk(directory):
    for file in files:
        if file.endswith("Stop.java") or file.endswith("Reset.java") or file.endswith("SingleStateTestRun.java"):
            filepath = os.path.join(root, file)
            with open(filepath, "r", encoding="utf-8") as f:
                content = f.read()
            
            new_content = pattern.sub(replacement, content)
            
            if new_content != content:
                with open(filepath, "w", encoding="utf-8") as f:
                    f.write(new_content)
                print(f"Updated {filepath}")
