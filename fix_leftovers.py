import re

file_path = r'src\main\java\com\tasnetwork\calibration\conveyor\remote\ProcalRemoteSender.java'
with open(file_path, 'r', encoding='utf-8') as f:
    content = f.read()

# Remove statusResponse loggers and declarations
content = re.sub(r'(?m)^\s*ApplicationLauncher\.logger\.debug\(.*statusResponse.*$', '', content)
content = re.sub(r'(?m)^\s*status = true;\s*$', '', content)
content = re.sub(r'(?m)^\s*String statusResponse = "";\s*$', '', content)

with open(file_path, 'w', encoding='utf-8') as f:
    f.write(content)
print('Fixed missing statusResponse and status usages.')
