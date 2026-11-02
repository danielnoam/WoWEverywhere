# TODO

Only work that's left. Shipped items move to DONE.md, rejected ones to DROPPED.md.

## Next: older private server versions, the rest

1.12.1, 2.4.3, 4.3.4 and 5.4.8 basic play is built (see DONE.md). What they still lack:

- **Controller Menus on 1.12.1 and 2.4.3.** Find which bytes of the 5875 and 8606 exes stop the "login interface files are corrupt" check, on Daniel's PC like the 3.3.5a ones, then port the login, realm and character Lua to each client's GlueXML. Add each to `LegacyGame.hasEverywherePatches` and give `WowExePatch` a spot list per build.
- **Grow the 1.12.1 and 2.4.3 addon catalogs.** Read the first workflow run's log to see which starter repos were kept, then add more from the community lists, the way `wotlk335.json` was filled.
- **Addon catalogs for 4.3.4 and 5.4.8.** Same as above, once Daniel says either runs well enough to play.
- **Find the fastest settings for 4.3.4 and 5.4.8.** Measure fps against 3.3.5a on the same spot, then decide whether their own lighter-graphics values or Box64 flags are worth it.

## Next: from upstream (WoWForeverywhere 0.6.0 and jaredgei 2.3.3, checked 2026-11-02)

Each is a content port of an upstream commit, kept to new files or thin edits.

- **Keep the game running when switching apps.** Add `suspendPolicy = never` in `baseContainerConfig` (WoWForeverScreen.kt) and the legacy container config, so a paused client doesn't drop its connection while Daniel reads the Warmane email code. Check battery drain on the Odin with the game in the background. Upstream: d62da45.
- **Resolution always follows Settings.** In `buildConfigWtf`, treat a resolution key as stale unless it equals the container's size, and drop `fitsScreen`. WoW saves its own 720p pick on exit, which otherwise keeps Settings, Resolution from applying. Do the same for the legacy `gxResolution` in LegacyClient.kt if it has the same problem. Upstream: d62da45.
- **WoW Forever defaults to the launch game (wowf).** Remove `WowProduct.releaseDate` and the date checks in BuildInfo and WowClientDownloader, so the launch build comes before the beta now that it's open. Upstream also added a Launch/Beta choice in Settings and a prompt for beta installs to switch (5095649, 0cca632); port the choice, skip the prompt unless Daniel wants it.
- **Adreno 8xx detection for Snapdragon 8s Gen 4 (SM8735) and 8 Gen 5 (SM8845).** Add both to `isAdreno8xxDevice`, plus upstream's renderer check (`Adreno 8xx` in the GPU name), and a unit test. Those phones get the 7xx driver today and the game finds no graphics card. Upstream: 65fcde5, d62da45.
- **Frame generation (LSFG) with the player's own Lossless.dll.** Settings imports the player's copy and has an on/off switch applied at launch; the layer is already bundled. New file like upstream's `wow/FrameGeneration.kt`, hooked into `LsfgVkManager`. Try it on 3.3.5a on the Odin: it adds input lag, so it only helps if 30 to 50 fps feels smoother with it. Never bundle the dll. Upstream: 1c4633c.
- **Download speed and time left while installing.** Port `install/DownloadSpeed.kt` and the download panel, with the installer counting network bytes separately and reporting during large files. Time left only shows when it can be estimated. Upstream: 60158e5.

## Later: 6.x and newer private servers

Daniel asked for this on 2026-10-29, gated on 5.4.8 being playable on the Odin. If 5.4.8 isn't, it isn't worth building.

- **Bundle FEX's ARM64EC library (`libarm64ecfex.dll`) so Proton can run x64 code.** Private server clients from 6.x on are x64 only. Blizzard's ARM64 build can't be used, because the servers' client patches (Arctium) only fit the old x64 builds. Fetch FEX from a pinned release in CI with a sha256, like wowbox64, and turn it on only for these profiles. Check that it doesn't change how the native ARM64 clients start.
- **Run the client patcher under emulation.** Arctium's launcher is an x64 exe that patches the client in memory at start. Start it as the profile's exe instead of Wow.exe, with the realm address it needs. Try it with one well-known build first (7.3.5 or 3.4.x private servers, whichever Daniel plays).
- **Measure before going further.** Compare frame rate with 5.4.8 on the same spot. x64 emulation through FEX is likely slower than wowbox64 on 32-bit code (a guess), on a client that's heavier again.
