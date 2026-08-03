**Helix**  
**KI-gestuetztes Health- und Calorie-Tracking mit lokaler Vorverarbeitung und Cloud-Coaching**  
**Veranstaltung:** Aufgabe Problemlosungs-Toolchain / "Build your own Agent"  
   
 **Hochschule:** Provadis Hochschule  
   
 **Autor:** Görkem Aymergen  
   
 **Matrikelnummer:** D815  
   
 **Dozent:** Prof. Dr. Richard Beetz  
   
 **Abgabe:** 11.08.2026  
*Dies ist ein Entwurf fuer die spaetere PDF-Abgabe. Die markierten Screenshot-Platzhalter muessen durch eigene Screenshots ersetzt werden.*  
**1. Kurzfassung**  
Helix ist eine Android-Anwendung zum Erfassen von Lebensmitteln, zur Gewichtskontrolle und zur Berechnung persoenlicher Kalorienziele. Die Anwendung kombiniert klassische Softwarekomponenten mit mehreren KI-Toolchains.  
Eingabe des Benutzers  
     -> lokale ML-Vorverarbeitung  
     -> Kontextanreicherung  
     -> Gemini-Agent in der Cloud  
     -> Entscheidung  
     -> UI-Karte oder Benachrichtigung  
   
   
**2. Ziele und Anwendungsfall**  
**2.1 Ziel**  
Ziel des Projekts ist die Entwicklung eines mobilen Tools, das nicht nur Kalorien protokolliert, sondern den Benutzer kontextbezogen unterstuetzt. Die Anwendung soll zeigen, wie lokale ML-Modelle und cloudbasierte generative KI sinnvoll kombiniert werden koennen.  
**2.2 Konkreter Anwendungsfall**  
Ein Benutzer erfasst eine Mahlzeit, beispielsweise eine Pizza. Die App kennt unter anderem:  
- das Lebensmittel und die Naehrwerte,  
- das aktuelle Kalorienbudget,  
- das persoenliche Ziel, beispielsweise Gewichtsabnahme,  
- die bisherige Tagesbilanz,  
- die Uhrzeit,  
- historische Ernaehrungstrends,  
- optional Kalender- und Wetterinformationen.  
Die App soll nicht jede Mahlzeit kritisieren. Eine gelegentliche Mahlzeit, die in das persoenliche Budget passt, soll keinen Alarm ausloesen. Erst wenn eine Mahlzeit im individuellen Kontext problematisch sein koennte, wird der Cloud-Coach zur genaueren Bewertung aufgerufen.  
**2.3 Warum diese Loesung sinnvoll ist**  
Eine einfache Kalorienliste koennte zwar Werte speichern, wuerde aber keinen kontextbezogenen Hinweis geben. Eine rein cloudbasierte KI-Loesung waere flexibler, wuerde aber jede Eingabe an einen externen Dienst senden und unnoetige API-Anfragen erzeugen.  
Helix kombiniert deshalb zwei Ansaetze:  
- Die lokale ML-Vorverarbeitung ist schnell, funktioniert auf dem Geraet und filtert viele normale Eintraege.  
- Gemini kann komplexe Zusammenhaenge und unstrukturierte Kontextinformationen interpretieren.  
Diese Kombination reduziert Cloud-Anfragen, senkt die Latenz bei normalen Eintraegen und nutzt die staerkere generative KI nur dort, wo eine genauere Bewertung sinnvoll ist.  
**2.4 Abgrenzung**  
Helix ist ein Schul- und Demonstrationsprojekt. Die Anwendung ist keine medizinische Software und liefert keine klinisch validierte Ernaehrungsberatung. Es gibt keine Benutzerkonten, keine Synchronisierung zwischen Geraeten und kein Backup-System.  
   
**3. Anforderungen**  
**3.1 Funktionale Anforderungen**  
| | | |  
|-|-|-|  
| **ID** | **Anforderung** | **Umsetzung** |   
| F1 | Benutzerprofil anlegen | Onboarding-Modul |   
| F2 | Kalorienziel berechnen | Mifflin-St-Jeor-Formel und CalorieCalculator |   
| F3 | Lebensmittel manuell erfassen | Dashboard und Room-Datenbank |   
| F4 | Lebensmittel bearbeiten und loeschen | Dashboard und FoodRepository |   
| F5 | Lebensmittel nach Mahlzeit gruppieren | Breakfast, Lunch, Dinner, Snack |   
| F6 | Produkt per Barcode erfassen | CameraX, ML Kit, Open Food Facts |   
| F7 | Lebensmittel per Foto schaetzen | ML Kit und Gemini Vision |   
| F8 | Gewicht dokumentieren | Tracking-Modul und Room |   
| F9 | Kontextbezogenes Coaching | DistilBERT, Gemini, WorkManager |   
| F10 | Benachrichtigung erzeugen | Android Notification API |   
   
**3.2 Nichtfunktionale Anforderungen**  
- Modularer und wartbarer Aufbau  
- Lokale Speicherung der Kern-Daten  
- Geringer manueller Aufwand nach dem Erfassen einer Mahlzeit  
- Nutzung kostenloser oder testbarer Cloud-Dienste im Rahmen des Projekts  
- Nachvollziehbare Toolchain und reproduzierbarer Build  
- Offenlegung der Grenzen von KI-Ergebnissen  
**4. Kurzskizze der Loesung**  
**4.1 Komponenten**  
| | | |  
|-|-|-|  
| **Komponente** | **Aufgabe** | **Ausfuehrungsort** |   
| Jetpack Compose | Benutzeroberflaeche | Smartphone |   
| Kotlin / Android | Anwendungslogik | Smartphone |   
| Room | Lokale Datenbank | Smartphone |   
| DataStore | Einstellungen und Praeferenzen | Smartphone |   
| CameraX | Kamerazugriff | Smartphone |   
| ML Kit Barcode Scanning | Barcode erkennen | Smartphone |   
| ML Kit Image Labeling | Food-/Nicht-Food-Pruefung | Smartphone |   
| Open Food Facts | Produkt- und Naehrwertdaten | Cloud/API |   
| DistilBERT + TensorFlow Lite | Lokaler Coaching-Gatekeeper | Smartphone |   
| Gemini API | Bildanalyse und Coaching | Cloud |   
| WorkManager | Hintergrundverarbeitung | Smartphone |   
| Open-Meteo | Wetterkontext | Cloud/API |   
   
**4.2 Hauptablauf: Coaching**  
FoodEntry wird gespeichert  
     |  
     v  
 DashboardViewModel baut FoodContext  
     |  
     v  
 DistilBertFoodClassifier.evaluate()  
     |  
     +-- niedrige Wahrscheinlichkeit -> kein Cloud-Aufruf  
     |  
     +-- ausreichende Wahrscheinlichkeit -> InvisibleCoachWorker  
                                       |  
                                       v  
                               Kalender, Wetter, Trends  
                                       |  
                                       v  
                                   Gemini API  
                                       |  
                          criticalAlert true/false  
                                       |  
                                       v  
                               Notification oder Ende  
   
**4.3 Hauptablauf: Barcode**  
Kamera oeffnen  
     -> CameraX liefert Frames  
     -> ML Kit erkennt Barcode  
     -> lokaler Produkt-Cache pruefen  
     -> Open Food Facts abfragen  
     -> Produktdaten anzeigen  
     -> Benutzer bestaetigt Eintrag  
   
**5. Softwarearchitektur**  
**5.1 Module**  
:app  
   Anwendungsshell, Navigation und Koin-Start  
   
 :feature:dashboard  
   Dashboard, Food-Log, Barcode-UI, Foto-UI, Coach-UI  
   
 :feature:tracking  
   Gewichtsverlauf und Progress-Screen  
   
 :feature:onboarding  
   Ersteinrichtung und persoenliches Profil  
   
 :feature:settings  
   Ziele, API-Key, Berechtigungen und Developer Tools  
   
 :core:data  
   Room, Repositories, APIs, KI-Services, WorkManager  
   
 :core:model  
   Plattformunabhaengige Datenmodelle  
   
 :core:ui  
   Theme und wiederverwendbare Compose-Komponenten  
   
**5.2 MVVM und Datenfluss**  
Die Benutzeroberflaeche ist mit ViewModels verbunden. Die ViewModels verwalten StateFlow-Zustaende und rufen Repositories oder Services auf. Repositories kapseln Room und externe Datenquellen.  
Compose Screen  
     -> ViewModel  
     -> Repository / Service  
     -> Room / DataStore / API / KI-Modell  
     -> StateFlow  
     -> Compose Screen  
   
**5.3 Dependency Injection**  
Koin erstellt zentrale Abhaengigkeiten. Das coreDataModule stellt Datenbank, DAOs, Repositories, Netzwerk, ML-Services, Gemini, WorkManager und den DistilBERT-Classifier bereit. Feature-Module stellen ihre jeweiligen ViewModels bereit.  
**5.4 Navigation**  
Die App verwendet zwei Navigationsebenen:  
- Onboarding oder Main als Startziel  
- Dashboard, Progress und Settings als Tabs innerhalb des Main-Bereichs  
**6. Detaillierte Beschreibung der KI-Toolchains**  
**6.1 Toolchain A: Gemini Vision**  
**Aufgabe**  
Gemini analysiert ein Foto eines Lebensmittels und schaetzt den Namen, die Portion und die Naehrwerte.  
**Technische Umsetzung**  
Dateien:  
- FoodVisionService.kt  
- VisionRepository.kt  
- FoodGatekeeper.kt  
Ablauf:  
1. Der Benutzer startet die Kamera im Add-Food-Dialog.  
2. Die App nimmt ein Bitmap auf.  
3. ML Kit prueft lokal, ob das Bild vermutlich Lebensmittel enthaelt.  
4. Gemini bekommt Bild und Prompt.  
5. Gemini soll ausschliesslich ein JSON-Objekt zurueckgeben.  
6. Kotlin Serialization wandelt das JSON in FoodRecognitionResult um.  
7. Der Benutzer kann die Schaetzung pruefen und vor dem Speichern anpassen.  
**Cloud-Daten**  
Das Bild wird fuer die Analyse an Gemini uebergeben. Dafuer benoetigt die App einen Gemini API-Key und Internetzugang.  
**6.2 Toolchain B: Lokaler DistilBERT-Gatekeeper**  
**Aufgabe**  
DistilBERT entscheidet nicht final, ob ein Benutzer gewarnt werden soll. Das Modell bewertet nur, ob ein Eintrag an Gemini zur genaueren Pruefung geschickt werden sollte.  
**Eingabe**  
Das Modell bekommt einen Text aus FoodContext:  
Food: pizza. Calories: 800cal. Protein: 20g. Fat: 35g.  
 Carbohydrates: 90g. Fiber: 4g. Time: 21h.  
 Remaining calories: -300cal. Daily target: 2000cal. Goal: Lose.  
   
**Ausgabe**  
Das Modell liefert zwei Logits. Die App berechnet daraus eine Wahrscheinlichkeit fuer Klasse 1. Bei der aktuellen Demo-Konfiguration wird ab 0.49 ein Gemini-Aufruf vorbereitet.  
**Lokale Ausfuehrung**  
Nach dem Training wurde das Modell als food_problem_detector.tflite exportiert und in den Android-Assets abgelegt. vocab.txt wird von einem kleinen lokalen WordPiece-Tokenizer verwendet.  
core/data/src/main/assets/food_problem_detector.tflite  
 core/data/src/main/assets/vocab.txt  
   
Die Inferenz erfolgt lokal auf dem Smartphone. Das Modell benoetigt keine Netzwerkverbindung.  
**6.3 Toolchain C: Gemini Invisible Coach**  
**Aufgabe**  
Gemini trifft die finale kontextbezogene Entscheidung und kann eine Benachrichtigung ausloesen.  
**Kontext**  
Der Prompt kann enthalten:  
- Aktueller Food-Log  
- Kalorienziel und verbleibende Kalorien  
- Benutzerziel  
- Historische Kalorien- und Proteintrends  
- Kalenderereignisse  
- Wetter  
**Agentencharakter**  
Der Invisible Coach besitzt die typischen Elemente eines einfachen Agenten-Workflows:  
- **Input:** neue Mahlzeit und Benutzerkontext  
- **Entscheidung:** lokaler Gatekeeper und Gemini  
- **Tools:** Room, DataStore, Kalender, Standort, Wetter-API  
- **Aktion:** Coach-Karte speichern und Notification erzeugen  
- **Automatisierung:** WorkManager fuehrt die Bewertung im Hintergrund aus  
Gemini kann jedoch nur die im Prompt gelieferten Informationen verwenden und ist kein autonomes System mit dauerhaftem Langzeitgedaechtnis.  
**6.4 Training und Export**  
Das Notebook train_gatekeeper.ipynb wird in Google Colab ausgefuehrt.  
PyTorch  
     -> Hugging Face Transformers / DistilBERT  
     -> Training des Classification Head  
     -> ONNX  
     -> onnx2tf  
     -> TensorFlow Lite  
     -> Android Asset  
   
Die DistilBERT-Basis wird eingefroren; nur der Klassifikationskopf wird trainiert. Die Datenbasis ist bewusst klein, da das Projekt ein funktionsfaehiger Demonstrator und kein produktionsreifes Modell ist.  
**7. Automatisierung und manueller Eingriff**  
**7.1 Automatisierte Schritte**  
Nach dem Speichern einer Mahlzeit laufen folgende Schritte automatisch:  
1. Berechnung des aktuellen Kalorienkontexts  
2. Lokale DistilBERT-Inferenz  
3. Entscheidung ueber den Cloud-Aufruf  
4. Start eines WorkManager-Jobs  
5. Abruf von Kalender-, Wetter- und Trenddaten  
6. Aufruf der Gemini API  
7. JSON-Auswertung  
8. Speicherung der Antwort  
9. Notification bei criticalAlert=true  
**7.2 Manueller Eingriff**  
Der Benutzer muss nur:  
- sein Profil ausfuellen,  
- Lebensmittel erfassen oder bestaetigen,  
- optional API-Key und Berechtigungen konfigurieren,  
- die initialen KI-Schaetzungen pruefen.  
Die eigentliche Coaching-Verarbeitung benoetigt danach keinen weiteren manuellen Start.  
**8. Konfiguration und Installation**  
**8.1 Voraussetzungen**  
- Android Studio Ladybug oder neuer  
- JDK 21  
- Gradle Wrapper aus dem Projekt  
- Android SDK Platform 36 und Extension Level 1  
- Android-Geraet mit API 34 oder hoeher  
- Gemini API-Key fuer Cloud-Funktionen  
**8.2 Build**  
cd /home/goerkem/StudioProjects/Helix  
 ./gradlew assembleDebug --no-configuration-cache  
   
**8.3 Installation auf dem Smartphone**  
USB-Debugging aktivieren und das Geraet autorisieren:  
adb devices  
 ./gradlew installDebug --no-configuration-cache  
   
Falls mehrere ADB-Verbindungen vorhanden sind, ein Geraet mit adb -s <serial> auswaehlen.  
**8.4 Gemini-Key**  
Fuer den Build kann ein Key in secrets.properties liegen:  
GEMINI_API_KEY=your_key_here  
   
Alternativ kann der Key in der App unter Settings eingegeben werden. Geheimnisse duerfen nicht in das Git-Repository eingecheckt werden.  
**9. Datenschutz und Kosten**  
**Lokal**  
- Food- und Gewichts-Daten werden lokal in Room gespeichert.  
- DistilBERT laeuft lokal.  
- ML Kit fuer Barcode und Image Labeling laeuft lokal.  
**Cloud**  
- Produkt-Barcodes koennen an Open Food Facts gesendet werden.  
- Fotos koennen an Gemini gesendet werden.  
- Fuer Coaching relevante Daten koennen an Gemini gesendet werden.  
- Wetterdaten werden bei vorhandenem Standort von Open-Meteo abgerufen.  
Die Nutzung der Cloud-Dienste ist abhaengig von API-Key, Quota, Netzwerk und den jeweiligen kostenlosen Kontingenten. Fuer die Abgabe sollten die verwendeten Konten und Limits dokumentiert werden, ohne geheime Schluessel zu veroeffentlichen.  
**10. Tests und Nachweis der Funktionsfaehigkeit**  
**10.1 Build-Test**  
Der Debug-Build wurde mit assembleDebug erfolgreich erzeugt und auf einem Android-16-Geraet installiert.  
**10.2 KI-Test: lokaler Gatekeeper**  
Beispieltest:  
1. Pizza mit ca. 800 kcal erfassen.  
2. Logcat oeffnen.  
3. Nach DistilBertFoodClassifier suchen.  
Erwartetes Log-Format:  
riskProbability=...  
 shouldEvaluate=true  
   
**10.3 KI-Test: Gemini-Coach**  
Nach einem akzeptierten Eintrag:  
Local model accepted food for coach evaluation  
 Worker started; apiKeyPresent=true  
 Gemini completed; criticalAlert=true  
 Worker result SUCCESS  
   
Bei criticalAlert=true sollte eine Notification erscheinen. Bei criticalAlert=false darf keine Warnung erscheinen.  
**10.4 Barcode-Test**  
1. Add-Food-Dialog oeffnen.  
2. Barcode-Symbol waehlen.  
3. Kamera-Berechtigung erteilen.  
4. Einen Produktbarcode scannen.  
5. Open-Food-Facts-Antwort pruefen.  
Wenn ein Produkt nicht gefunden wird, zeigt die App einen einzelnen Fehler und beendet den Scanner. Ein neuer Versuch startet erst nach erneutem Oeffnen des Scanners.  
**10.5 Relevante Logcat-Befehle**  
adb logcat | grep -E "DistilBertFoodClassifier|CoachGate|InvisibleCoachWorker|FoodVisionService|OpenFood"  
   
**11. Screenshots fuer die PDF-Abgabe**  
Die folgenden Bilder sollten im finalen Cookbook enthalten sein:  
1. Android-Studio-Projektstruktur  
2. Onboarding mit Profilfeldern  
3. Dashboard mit Kalorien- und Makroanzeige  
4. Manueller Add-Food-Dialog  
5. Barcode-Scanner  
6. Erfolgreich geladene Barcode-Naehrwerte  
7. Fotoanalyse mit Gemini  
8. Progress-Screen mit Gewichtsverlauf  
9. Settings mit Gemini-Key-Feld, ohne sichtbaren echten Key  
10. Notification des Invisible Coach  
11. Google-Colab-Notebook mit Trainingszellen  
12. Colab-Ausgabe von model_float32.tflite  
13. Terminal mit erfolgreichem Gradle-Build  
14. Logcat mit DistilBERT- und Gemini-Nachweis  
Geheime API-Keys, private Kalenderdaten und persoenliche Gesundheitsdaten muessen vor Screenshots unkenntlich gemacht werden.  
**12. Fehler und Loesungen waehrend der Entwicklung**  
**Android SDK**  
Der Build schlug zuerst fehl, weil die Platform-Dateien fuer Android 36 und 36.1 unvollstaendig installiert waren. Nach der Installation beider Android-Plattformen waren die Dateien android-36/android.jar und android-36.1/android.jar vorhanden.  
**TFLite Float16**  
Der erste Float16-Export konnte auf dem Smartphone nicht geladen werden:  
Type 'FLOAT16' is not supported by gather  
   
Deshalb wurde der Float32-Export verwendet.  
**TFLite Eingabetyp**  
Der exportierte Float32-Graph erwartete auch fuer die Textinputs Float32-Werte. Der Android-Wrapper wurde angepasst, damit er INT64, INT32 und FLOAT32 anhand des Tensor-Typs unterstuetzt.  
**Kotlin Serialization**  
Der Barcode-Service konnte ProductResponse zuerst nicht konvertieren, weil das Kotlin-Serialization-Plugin im core:data-Modul fehlte. Das Plugin wurde im Modul aktiviert.  
**Wiederholte Barcodescans**  
Bei einer fehlgeschlagenen Produktsuche blieb der Scanner geoeffnet und reichte denselben Barcode wiederholt ein. Der Scanner wird jetzt bei Erfolg oder Fehler geschlossen und akzeptiert bis zum manuellen Neustart nur einen Scan.  
**13. Quellcode und ZIP-Abgabe**  
Fuer die Abgabe sollte ein ZIP ohne unnoetige Build-Artefakte erstellt werden. Enthalten sein sollten mindestens:  
- app/  
- core/  
- feature/  
- baselineprofile/  
- gradle/  
- build.gradle.kts  
- settings.gradle.kts  
- gradle.properties  
- train_gatekeeper.ipynb  
- documentation/  
- README.md  
Nicht enthalten sein sollten:  
- secrets.properties  
- private API-Keys  
- lokale IDE-Konfiguration, sofern nicht erforderlich  
- .gradle/  
- build/  
- private Nutzerdaten  
Da das TFLite-Modell ungewoehnlich gross ist, sollte die Abgabe pruefen, ob die Kursplattform die Datei akzeptiert. Falls nicht, kann das Modell separat zusammen mit einer genauen Installationsanleitung abgegeben werden.  
**14. Kritische Reflexion**  
Die Loesung ist fuer den Demonstrationszweck funktionsfaehig, aber nicht produktionsreif. Der lokale Datensatz ist klein und die Wahrscheinlichkeiten des Gatekeepers sind nur begrenzt aussagekraeftig. Die finale Qualitaet der Coaching-Antwort haengt von Gemini, Netzwerk, API-Quota und Prompt-Auswertung ab.  
Die Architektur ist trotzdem sinnvoll, weil sie unterschiedliche Staerken kombiniert: lokale schnelle Verarbeitung, strukturierte lokale Speicherung und cloudbasierte generative Kontextauswertung. Ausserdem wird sichtbar, wie ein KI-Agent mit Tools, Datenquellen, Regeln und automatisierten Aktionen in eine mobile Anwendung integriert werden kann.  
**15. Fazit**  
Helix zeigt eine konkrete Problemlosungs-Toolchain fuer einen alltagsnahen Anwendungsfall. Der Benutzer liefert nur die Mahlzeit als Eingabe. Danach werden lokale ML-Modelle, Datenbanken, APIs, ein cloudbasierter generativer Agent und eine mobile Benachrichtigung automatisch miteinander verbunden.  
Die wesentliche technische Erkenntnis ist die Kombination aus lokaler und cloudbasierter KI:  
- lokale Modelle fuer Geschwindigkeit und Vorfilterung,  
- Gemini fuer flexible Interpretation und Kontextbewertung,  
- WorkManager fuer automatisierte Hintergrundausfuehrung,  
- Android-UI und Notifications fuer die Rueckgabe an den Benutzer.  
**Anhang: Praesentationsentwurf fuer 10 bis 15 Minuten**  
**Folie 1: Titel und Problem**  
- Helix  
- Health- und Calorie-Tracking  
- Problem: reine Protokollierung liefert wenig Kontext  
**Folie 2: Ziel und Anwendungsfall**  
- Mahlzeit erfassen  
- persoenliches Ziel beruecksichtigen  
- nur sinnvolle Warnungen erzeugen  
**Folie 3: Architektur**  
- Moduldiagramm  
- lokale Datenhaltung  
- Feature-Module  
**Folie 4: KI-Toolchain**  
- ML Kit lokal  
- DistilBERT lokal  
- Gemini in der Cloud  
- WorkManager und Notification als Automatisierung  
**Folie 5: Ablaufdiagramm**  
Food Entry -> DistilBERT -> WorkManager -> Gemini -> Notification  
   
**Folie 6: Live-Demo Setup**  
- Smartphone verbunden  
- App installiert  
- Gemini-Key vorkonfiguriert  
- Benachrichtigungen aktiviert  
**Folie 7: Live-Demo Food Log**  
- Pizza erfassen  
- lokales Modell akzeptiert den Eintrag  
- Gemini-Coach wird gestartet  
- Notification zeigen  
**Folie 8: Gegenbeispiel**  
- Salat erfassen  
- Gemini kann criticalAlert=false zurueckgeben  
- keine Warnung zeigen  
**Folie 9: Barcode oder Foto**  
- Barcode scannen und Open-Food-Facts-Daten zeigen  
- alternativ Foto aufnehmen und Gemini-Schaetzung zeigen  
**Folie 10: Reflexion**  
- Was funktioniert gut?  
- Wo liegen Grenzen?  
- Warum lokale und Cloud-KI kombinieren?  
- Was waere der naechste Entwicklungsschritt?  
**Zeitplanung**  
| | |  
|-|-|  
| **Abschnitt** | **Zeit** |   
| Problem und Ziel | 2 min |   
| Architektur und Toolchain | 3 min |   
| KI-Ablauf | 2 min |   
| Live-Demo | 5 min |   
| Grenzen und Fazit | 2 min |   
   
