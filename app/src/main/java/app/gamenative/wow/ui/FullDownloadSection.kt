package app.gamenative.wow.ui

import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import app.gamenative.wow.install.FullDownloader
import app.gamenative.wow.install.InstallController
import app.gamenative.wow.install.InstallRequest
import app.gamenative.wow.install.InstallUiState
import app.gamenative.wow.ui.components.FwButton
import app.gamenative.wow.ui.components.FwButtonKind
import app.gamenative.wow.ui.components.FwDownloadPanel
import app.gamenative.wow.ui.components.FwProgress
import app.gamenative.wow.ui.components.rememberDownloadSpeed
import app.gamenative.wow.ui.theme.Fw
import java.io.File

// Ported from upstream's GameFilesScreen (2ec324c), kept in its own file.
/** The full download behind [state], if that's what is running, paused or failed. */
internal val InstallUiState.fullRequest: InstallRequest?
    get() = when (this) {
        is InstallUiState.Running -> request
        is InstallUiState.Paused -> request
        is InstallUiState.Failed -> request
        else -> null
    }?.takeIf { it.full }

/** Game files, Download all game data: everything the game would otherwise stream, with Overnight mode for long runs. */
@Composable
internal fun FullDownloadSection(launcher: LauncherController, state: InstallUiState, onOpenOvernight: () -> Unit) {
    val context = LocalContext.current
    val product = launcher.files.product ?: return
    val root = File(launcher.gamePath)
    val complete = remember(launcher.gamePath, state) { FullDownloader.isComplete(root, product) }
    val otherDownload = state.fullRequest == null && (state is InstallUiState.Running || state is InstallUiState.Paused)
    val notifications = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {}
    LaunchedEffect(state) {
        if ((state as? InstallUiState.Done)?.full == true) InstallController.reset()
    }

    FwSection("Download all game data") {
        val running = state as? InstallUiState.Running
        val speed = rememberDownloadSpeed(running?.progress, windowMs = 15_000)
        when {
            running != null && running.request.full -> {
                FwDownloadPanel("Downloading all game data", running.progress, speed)
                Text(
                    "You can leave the app or keep playing while it downloads. The new data takes over the next time the game is closed.",
                    style = Fw.Secondary,
                )
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    FwButton("Overnight mode", onOpenOvernight)
                    FwButton("Pause", { InstallController.pause(context) }, kind = FwButtonKind.Ghost)
                }
            }
            state is InstallUiState.Paused && state.request.full -> {
                Text(state.reason ?: "Download paused. Everything downloaded so far is kept.", style = Fw.Text)
                state.progress?.let { FwProgress(it.fraction) }
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    FwButton("Resume", { InstallController.resume(context) })
                    FwButton("Cancel", { InstallController.cancel(context) }, kind = FwButtonKind.Ghost)
                }
            }
            state is InstallUiState.Failed && state.request.full -> {
                Text("The download stopped: ${state.message}", style = Fw.Text.copy(color = Fw.Warn))
                Text("Everything downloaded so far is kept, and trying again picks up where it stopped.", style = Fw.Secondary)
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    FwButton("Try again", { InstallController.resume(context) })
                    FwButton("Cancel", { InstallController.cancel(context) }, kind = FwButtonKind.Ghost)
                }
            }
            complete -> Text(
                "All game data is on this device, so the game doesn't download world data while you play. After a game update it downloads only the new files.",
                style = Fw.Text,
            )
            else -> {
                Text(
                    "The game normally downloads its world data while you play. On a slow connection that means long loading screens and missing text or characters. " +
                        "Download all of it now instead, about 50 GB from Blizzard's servers.",
                    style = Fw.Text,
                )
                Text(
                    "On a slow connection this can take all night: start it, open Overnight mode, and leave the device on its charger.",
                    style = Fw.Secondary,
                )
                FwButton(
                    "Download everything",
                    {
                        if (Build.VERSION.SDK_INT >= 33) notifications.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                        InstallController.start(context, InstallRequest(root, product, full = true))
                    },
                    enabled = !launcher.busy && !otherDownload,
                )
            }
        }
    }
}
