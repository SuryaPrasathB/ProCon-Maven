import os
import re

dir_path = r"d:\SURYA\.DEVELOPMENT\.PROJECTS\.CONVEYOR PROJECT\ProCON\ProCon-Maven-s0.9.2.8\src\main\java"
count = 0
for root, dirs, files in os.walk(dir_path):
    for file in files:
        if file.endswith(".java"):
            file_path = os.path.join(root, file)
            try:
                with open(file_path, "r", encoding="utf-8") as f:
                    content = f.read()
                
                new_content = re.sub(r'\bApplicationLauncher\.InformUser\b', 'WindowManager.InformUser', content)
                new_content = re.sub(r'\bApplicationLauncher\.setCursor\b', 'WindowManager.setCursor', new_content)
                
                if new_content != content:
                    with open(file_path, "w", encoding="utf-8") as f:
                        f.write(new_content)
                    print(f"Updated {file_path}")
                    count += 1
            except Exception as e:
                print(f"Error processing {file_path}: {e}")

print(f"Total files updated: {count}")
