import re

def fix_dual(filepath):
    with open(filepath, 'r', encoding='utf-8') as f:
        content = f.read()

    # We want to find:
    # 			switch (bayKey) { ... }
    #			// Also trigger MainControlPaneController UI updates if instance is available
    #			com.tasnetwork.calibration.conveyor.dashboard.MainControlPaneController mcpc = 
    #				com.tasnetwork.calibration.conveyor.dashboard.MainControlPaneController.getInstance();
    #			if (mcpc != null) { switch (bayKey) { ... } }
    
    # We will replace it with:
    #			com.tasnetwork.calibration.conveyor.dashboard.MainControlPaneController mcpc = 
    #				com.tasnetwork.calibration.conveyor.dashboard.MainControlPaneController.getInstance();
    #			if (mcpc != null) { switch (bayKey) { ... } } else { switch (bayKey) { ... } }

    pattern = re.compile(r'(switch\s*\(bayKey\)\s*\{[^\}]+\})\s*(// Also trigger[^\n]*\n)?\s*com\.tasnetwork\.calibration\.conveyor\.dashboard\.MainControlPaneController\s+mcpc\s*=\s*com\.tasnetwork\.calibration\.conveyor\.dashboard\.MainControlPaneController\.getInstance\(\);\s*if\s*\(mcpc\s*!=\s*null\)\s*\{\s*(switch\s*\(bayKey\)\s*\{[^\}]+\})\s*\}', re.DOTALL)
    
    def repl(m):
        local_switch = m.group(1)
        mcpc_switch = m.group(3)
        return f"com.tasnetwork.calibration.conveyor.dashboard.MainControlPaneController mcpc = \n\t\t\t\tcom.tasnetwork.calibration.conveyor.dashboard.MainControlPaneController.getInstance();\n\t\t\tif (mcpc != null) {{\n\t\t\t\t{mcpc_switch}\n\t\t\t}} else {{\n\t\t\t\t{local_switch}\n\t\t\t}}"

    content, num = pattern.subn(repl, content)
    print(f"Replaced {num} occurrences in {filepath}")
    
    with open(filepath, 'w', encoding='utf-8') as f:
        f.write(content)

if __name__ == '__main__':
    fix_dual(r'd:\SURYA\.DEVELOPMENT\.PROJECTS\.CONVEYOR PROJECT\ProCON\ProCon-Maven\src\main\java\com\tasnetwork\calibration\conveyor\StateExecutorController.java')
