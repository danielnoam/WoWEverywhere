package app.gamenative.wow.ui

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import app.gamenative.wow.FrameGeneration
import app.gamenative.wow.ui.components.FwButton
import app.gamenative.wow.ui.components.FwButtonKind
import app.gamenative.wow.ui.components.FwRow
import app.gamenative.wow.ui.theme.Fw
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val HOW =
    "Lossless Scaling's frame generation (LSFG) adds generated frames for a smoother picture, at the cost of a little input delay. " +
        "It needs Lossless.dll from your own copy of Lossless Scaling on Steam (Steam/steamapps/common/Lossless Scaling on your PC). " +
        "Copy it to this device and choose it here. The in-game menu then has an LSFG tab to set it up."

/** Settings, App: frame generation with the player's own Lossless.dll, for every profile. */
@Composable
internal fun FrameGenerationChoice(setHint: (String) -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var hasDll by remember { mutableStateOf(FrameGeneration.hasDll(context)) }
    var enabled by remember { mutableStateOf(FrameGeneration.isEnabled(context)) }
    var message by remember { mutableStateOf<String?>(null) }
    val picker = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        if (uri == null) return@rememberLauncherForActivityResult
        scope.launch {
            val result = withContext(Dispatchers.IO) {
                runCatching { context.contentResolver.openInputStream(uri) }.getOrNull()
                    ?.let { FrameGeneration.import(context, it) } ?: FrameGeneration.ImportResult.FAILED
            }
            message = when (result) {
                FrameGeneration.ImportResult.OK -> null
                FrameGeneration.ImportResult.NOT_A_DLL -> "That file isn't Lossless.dll. Pick the one from your Lossless Scaling folder."
                FrameGeneration.ImportResult.FAILED -> "Couldn't read that file. Try copying it to the device again."
            }
            hasDll = FrameGeneration.hasDll(context)
            enabled = FrameGeneration.isEnabled(context)
        }
    }
    Text("Frame generation", style = Fw.Text, modifier = Modifier.padding(top = 8.dp))
    if (hasDll) {
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            listOf(true to "On", false to "Off").forEach { (value, label) ->
                FwRow(
                    selected = enabled == value,
                    onClick = {
                        FrameGeneration.setEnabled(context, value)
                        enabled = value
                    },
                    modifier = Modifier.hint("$HOW A change applies the next time you start the game.", setHint),
                ) {
                    Text(label, style = Fw.Text, modifier = Modifier.weight(1f))
                    if (enabled == value) Text("Selected", style = Fw.Data.copy(color = Fw.Brass))
                }
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            FwButton(
                "Choose another file",
                { picker.launch(arrayOf("*/*")) },
                modifier = Modifier.hint("Replaces the saved Lossless.dll.", setHint),
                kind = FwButtonKind.Ghost,
            )
            FwButton(
                "Remove",
                {
                    FrameGeneration.remove(context)
                    hasDll = false
                    enabled = FrameGeneration.isEnabled(context)
                },
                modifier = Modifier.hint("Deletes the saved Lossless.dll from this device and turns frame generation off.", setHint),
                kind = FwButtonKind.Ghost,
            )
        }
    } else {
        Text("Off: no Lossless.dll", style = Fw.Secondary)
        FwButton(
            "Choose Lossless.dll",
            { picker.launch(arrayOf("*/*")) },
            modifier = Modifier.hint(HOW, setHint),
            kind = FwButtonKind.Ghost,
        )
    }
    message?.let { Text(it, style = Fw.Secondary.copy(color = Fw.Warn)) }
}
