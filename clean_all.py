import re

file_path = r'src\main\java\com\tasnetwork\calibration\conveyor\remote\ProcalRemoteSender.java'
with open(file_path, 'r', encoding='utf-8') as f:
    content = f.read()

# Remove multiline comments /* ... */
content = re.sub(r'/\*.*?\*/', '', content, flags=re.DOTALL)

# Remove single line comments that start at the beginning of the line
content = re.sub(r'(?m)^\s*//.*\n', '', content)
content = content.replace(';// getServerDetails( terminalId, clusterId);', ';')
content = content.replace(';//"getTpIdResult" ;//"start";', ';')
content = content.replace(';//"start";', ';')
content = content.replace(' //validate for server access', '')
content = content.replace(';//+inputPortId);', ';')


# Fix Warnings
content = re.sub(r'(?m)^\s*boolean status = false;\n', '', content)
content = re.sub(r'(?m)^\s*status = true;\n', '', content)
content = re.sub(r'(?m)^\s*String statusResponse = "";\n', '', content)

# Remove unused variables entirely
content = re.sub(r'(?m)^\s*String ipAddress = "[^"]+";\n', '', content)
content = re.sub(r'(?m)^\s*String ipPort = "[^"]+";\n', '', content)
content = re.sub(r'(?m)^\s*ClusterServer procalVerifyServer = new ClusterServer\(ipAddress,ipPort\);\n', '', content)
content = re.sub(r'(?m)^\s*RestApiJsonBodyResponse clusterResponseData = new RestApiJsonBodyResponse\(\);\n', '', content)
content = re.sub(r'(?m)^\s*import com.tasnetwork.calibration.conveyor.RestApiJsonBodyResponse;\n', '', content)

# responseData
# Replace `String responseData = ...` with just the method call, EXCEPT in sendCommAllResultToProcal
# Wait, sendCommAllResultToProcal uses `responseData = ...` without `String ` in front of it!
# So we can safely replace `String responseData = ` with ` `
content = re.sub(r'(?m)^\s*String responseData = procalClientManager\.getAsyncProcalClient\(\)\.getResponseData\(\);\n', '			procalClientManager.getAsyncProcalClient().getResponseData();\n', content)

# myProcalRemoteResponse
# Remove the declaration if it's not used, but it's easier to just remove it from methods where we KNOW it's unused.
# It is used (returned) in sendParamStatusCommandToProcal and sendCommResultRefreshToProcal.
# In all other methods, it's just declared and assigned. Let's find those and remove the declaration and the "myProcalRemoteResponse = " part.
# Wait, ProcalRemoteResponse is returned only by methods returning ProcalRemoteResponse.
# We can find methods that return String, and inside them remove ProcalRemoteResponse stuff.
lines = content.split('\n')
in_string_method = False
new_lines = []
for line in lines:
    if 'public String ' in line:
        in_string_method = True
    elif 'public ProcalRemoteResponse ' in line:
        in_string_method = False
    
    if in_string_method:
        if 'ProcalRemoteResponse myProcalRemoteResponse = new ProcalRemoteResponse();' in line:
            continue
        if 'myProcalRemoteResponse  = procalClientManager.getAsyncProcalClient().getProcalVerifyRemoteResponse();' in line:
            line = line.replace('myProcalRemoteResponse  = ', '')
    
    # Also remove selectRunCommandMessage in sendSelectProjectRunScreenConfirmationCommand where it's unused
    if 'String selectRunCommandMessage = "isTestRunScreenDisplayed" ;' in line:
        continue

    new_lines.append(line)

content = '\n'.join(new_lines)

# Remove extra empty lines (more than 2 contiguous newlines -> 2)
content = re.sub(r'\n{3,}', '\n\n', content)

with open(file_path, 'w', encoding='utf-8') as f:
    f.write(content)
print('Cleanup complete.')
