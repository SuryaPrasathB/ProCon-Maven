import re

file_path = r'src\main\java\com\tasnetwork\calibration\conveyor\remote\ProcalRemoteSender.java'
with open(file_path, 'r', encoding='utf-8') as f:
    content = f.read()

# Remove 'boolean status = false;' and 'status = true;'
content = re.sub(r'(?m)^\s*boolean status = false;\n', '', content)
content = re.sub(r'(?m)^\s*status = true;\n', '', content)

# Remove unused procalVerifyServer and ipAddress/ipPort
content = re.sub(r'(?m)^\s*ClusterServer procalVerifyServer = new ClusterServer\(ipAddress,ipPort\);\n', '', content)
content = re.sub(r'(?m)^\s*String ipAddress = "[^"]+";\n', '', content)
content = re.sub(r'(?m)^\s*String ipPort = "[^"]+";\n', '', content)

# Remove unused RestApiJsonBodyResponse
content = re.sub(r'(?m)^\s*RestApiJsonBodyResponse clusterResponseData = new RestApiJsonBodyResponse\(\);\n', '', content)
content = re.sub(r'(?m)^\s*import com.tasnetwork.calibration.conveyor.RestApiJsonBodyResponse;\n', '', content)

# Remove unused responseData declarations in specific lines
# If a method doesn't use responseData, it warns. Let's just remove the declaration where it's unused.
# In most methods, it just calls `String responseData = procalClientManager.getAsyncProcalClient().getResponseData();` and never uses it.
content = re.sub(r'(?m)^\s*String responseData = procalClientManager\.getAsyncProcalClient\(\)\.getResponseData\(\);\n', '', content)

# Wait, in sendCommAllResultToProcal, it's declared at top `String responseData = "";`
# and then `responseData = procalClientManager...`
# That means in sendCommAllResultToProcal, `responseData = procalClientManager.getAsyncProcalClient().getResponseData();` is used without 'String' in front of it.
# So removing the `String responseData = ...` line is perfectly safe for other methods.

# In some methods: 'ProcalRemoteResponse myProcalRemoteResponse = new ProcalRemoteResponse();' is unused if the method returns String.
# If it's unused, let's remove it and its assignment.
content = re.sub(r'(?m)^\s*ProcalRemoteResponse myProcalRemoteResponse = new ProcalRemoteResponse\(\);\n', '', content)
content = re.sub(r'(?m)^\s*myProcalRemoteResponse\s*=\s*procalClientManager\.getAsyncProcalClient\(\)\.getProcalVerifyRemoteResponse\(\);\n', '', content)

# In sendSelectRunCommandToProcal (one of them), `String selectRunCommandMessage = "isTestRunScreenDisplayed" ;` might be unused?
# Oh wait, `selectRunCommandMessage` in sendSelectProjectRunScreenConfirmationCommand:
# String selectRunCommandMessage = "isTestRunScreenDisplayed" ;
# procalClientManager.sendPostCommandServer(clusterServer, endPoint,  commandMessage, requestBodyMap); <-- commandMessage used instead!
content = content.replace('String selectRunCommandMessage = "isTestRunScreenDisplayed" ;', '')


# Let's fix missing imports if needed, but we just removed one.

with open(file_path, 'w', encoding='utf-8') as f:
    f.write(content)
print('Cleanup warnings complete.')
