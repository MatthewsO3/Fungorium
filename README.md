# Fungorium
A projekt az egyetemi Szoftver Projekt Laboratórium tárgy keretében készült.
A félév során 5 fős csapatban együtt dolgozva terveztük, modelleztük és implementáltuk a kiadott játékot.
A kb. 240 oldalas dokumentáció fázisokra bontva rögzíti a fejlesztés menetét,tervezési lépéseit, illetve a megvalósítás módját. 

Specifikáció:
A játék Java nyelven készült, a grafikus megjelenítéshez pedig a Swing-et használtuk.
JDK21 vagy újabb verzió szükséges a futtatáshoz

Fordítás és futtatás:
Powershellben:
javac -d out -cp src (Get-ChildItem -Path src -Filter *.java -Recurse | ForEach-Object { $_.FullName })

jar cfm fung.jar MANIFEST.MF -C out .

java -jar fung.jar
