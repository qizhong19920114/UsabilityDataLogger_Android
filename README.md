# UsabilityDataLogger_Android

Usability Data Logger is presented by UESEO LLC. The function includes screen touch recording, keystroke recording, application runtime and URL
visit. 


Need to install SoftKeyboard to record the keyboard strokes

The main app is in the TabExample folder. 
- Touch record is done by accessing unix system device file using jni function 
- Keystroke record is done in SoftKeyboard app and the Main app just adds a signal file for starting the record
- Runtime monitoring is done by calling a thread recursively with 100 ms delay. On each thead, SampleActivityOnce() method is called to sample the current running apps. The runtime is calculated in the code for Analysis tab. 
- URL data is from the browser history. 


Current testing phone version: 4.4.4, Samsung Galaxy Note3; Jailbreaked
