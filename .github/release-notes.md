# WoWEverywhere 1.2.0

- **A wrong Lossless.dll no longer stops the game.** A Lossless.dll without the frame generation shaders made the game close as it started. The app now checks the file when you add it, warns in Settings if an older one is wrong, and starts the game without frame generation if it is. From upstream WoW Foreverywhere.
- **Backing out of a stuck start no longer freezes the app.** If the game hung while starting, leaving it could freeze the app until Android closed it. It now gives up after two seconds. From upstream WoW Foreverywhere.
- **The game starts on Android 17.** It hung at start on Android 17; the app now loads a simpler file-access helper there. From upstream WoW Foreverywhere.
- **Choose your WoW game account.** If your Battle.net login has more than one WoW account, such as WoW1 and WoW2, pick the one to play in Settings, Battle.net. From upstream WoW Foreverywhere.
- **Help for a blocked Blizzard login.** Settings, Battle.net has Fix a blocked login, with the steps for errors BLZ51914400 and BLZ51914500 and a link to battle.net. From upstream WoW Foreverywhere.
- **Download all game data, with Overnight mode.** Game files can download everything WoW Forever and the official versions would otherwise stream while you play, about 50 GB for WoW Forever, so there are no long loading screens on a slow connection. Overnight mode keeps the screen dark and the download running while the device charges. From upstream WoW Foreverywhere.
- **Addon manager mode.** If you play WoW some other way, the app can manage just that install's addons. Turn it on from the welcome screen or Settings, Addon manager mode, and pick the AddOns folder. Turning it off brings back your profiles. From upstream WoW Foreverywhere.

## Install

Download **WoWEverywhere.apk** below, or update from inside the app. It installs over 1.1.0 and keeps your settings, saved logins, game files and addons.

If Android says the app "conflicts with an existing package", your current copy came from somewhere else, such as the upstream WoW Foreverywhere releases. Uninstall it first, then install this one. Your game files and addons stay on your storage.

New to the app? See the [README](https://github.com/danielnoam/WoWEverywhere#getting-started) for setup.
