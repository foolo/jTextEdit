[Setup]
AppName=TextEditor
AppVersion=0.1
DefaultDirName={pf}\TextEditor
DisableProgramGroupPage=yes
UninstallDisplayIcon={app}\TextEditor.exe

[Files]
Source: "TextEditor.exe"; DestDir: "{app}"
Source: "dist\TextEditor.jar"; DestDir: "{app}\dist"
Source: "dist\lib\*"; DestDir: "{app}\dist\lib"

[Icons]
Name: "{group}\TextEditor"; Filename: "{app}\TextEditor.exe"

[Registry]
Root: HKCR; Subkey: "*\shell\Edit with TextEditor"; Flags: uninsdeletekey
Root: HKCR; Subkey: "*\shell\Edit with TextEditor"; ValueType: string; ValueName: "Icon"; ValueData: "{app}\TextEditor.exe"
Root: HKCR; Subkey: "*\shell\Edit with TextEditor\Command"; ValueType: string; ValueData: "{app}\TextEditor.exe %1"
