import re

file_path = r'src\main\java\com\tasnetwork\calibration\conveyor\remote\ProcalRemoteSender.java'
with open(file_path, 'r', encoding='utf-8') as f:
    content = f.read()

# Remove multiline comments /* ... */
content = re.sub(r'/\*.*?\*/', '', content, flags=re.DOTALL)

# Remove single line comments that start at the beginning of the line
content = re.sub(r'(?m)^\s*//.*\n', '', content)

# Specific inline comment removals
content = content.replace(';// getServerDetails( terminalId, clusterId);', ';')
content = content.replace(';//"getTpIdResult" ;//"start";', ';')
content = content.replace(';//"start";', ';')
content = content.replace(' //validate for server access', '')
content = content.replace(';//+inputPortId);', ';')

# Remove extra empty lines (more than 2 contiguous newlines -> 2)
content = re.sub(r'\n{3,}', '\n\n', content)

with open(file_path, 'w', encoding='utf-8') as f:
    f.write(content)
print('Cleanup complete.')
