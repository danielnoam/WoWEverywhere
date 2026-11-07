package app.gamenative.wow.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import app.gamenative.wow.product.GameAccounts
import app.gamenative.wow.ui.components.FwButton
import app.gamenative.wow.ui.components.FwButtonKind
import app.gamenative.wow.ui.components.FwChip
import app.gamenative.wow.ui.components.FwDialog
import app.gamenative.wow.ui.components.Links
import app.gamenative.wow.ui.theme.Fw

/** Settings, Battle.net: which WoW account to play when one login holds several (from upstream). */
@Composable
internal fun GameAccountChoice(setHint: (String) -> Unit) {
    val context = LocalContext.current
    var current by remember { mutableStateOf(GameAccounts.chosen) }
    fun pick(number: Int?) {
        GameAccounts.save(context, number)
        current = number
    }
    val hint = "If your Battle.net login has more than one WoW account, such as WoW1 and WoW2, choose the one to play. " +
        "The Battle.net app lists them in its account menu. A change applies the next time you start the game."
    Text("Game account", style = Fw.Text, modifier = Modifier.padding(top = 8.dp))
    Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        FwChip("The game's choice", current == null, { pick(null) }, modifier = Modifier.hint(hint, setHint))
        GameAccounts.NUMBERS.forEach { n -> FwChip(GameAccounts.name(n), current == n, { pick(n) }, modifier = Modifier.hint(hint, setHint)) }
    }
}

/**
 * Settings, Battle.net: Blizzard locks direct game logins from a network the account hasn't used (BLZ51914400,
 * often after a CAPTCHA), and one sign-in on the web lifts it. BLZ51914500 is a password that didn't match (from upstream).
 */
@Composable
internal fun BlockedLoginHelp(setHint: (String) -> Unit) {
    val context = LocalContext.current
    var open by remember { mutableStateOf(false) }
    FwButton(
        "Fix a blocked login",
        { open = true },
        modifier = Modifier.hint(
            "If the game says \"You have been disconnected\" with BLZ51914400 or BLZ51914500 right after you log in, Blizzard is blocking the sign-in.",
            setHint,
        ),
        kind = FwButtonKind.Ghost,
    )
    if (open) {
        FwDialog(
            title = "Blocked login",
            message = "Blizzard blocks game logins from a network it hasn't seen your account use, such as mobile data or a new Wi-Fi, " +
                "or a login that arrives over Wi-Fi and mobile data at once.",
            confirm = "Open battle.net",
            onConfirm = {
                open = false
                Links.open(context, Links.BATTLE_NET)
            },
            onDismiss = { open = false },
            dismiss = "Close",
        ) {
            listOf(
                "1. Turn off any phone setting that uses Wi-Fi and mobile data together, such as Optimize network for speed in Settings, System, Network.",
                "2. Choose Open battle.net and sign in there, on the same connection.",
                "3. Come back to the app and start the game again.",
                "4. Still getting BLZ51914500? Your password didn't match. Enter it again with Change login.",
            ).forEach { Text(it, style = Fw.Text) }
        }
    }
}
