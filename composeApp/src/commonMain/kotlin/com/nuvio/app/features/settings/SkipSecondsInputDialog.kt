package com.nuvio.app.features.settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.nuvio.app.core.ui.DialogButton
import com.nuvio.app.core.ui.DialogButtonStyle
import com.nuvio.app.core.ui.DialogButtons
import com.nuvio.app.core.ui.DialogOption
import com.nuvio.app.core.ui.DialogSurface
import com.nuvio.app.features.player.PLAYER_SKIP_SECONDS_MAX
import com.nuvio.app.features.player.PLAYER_SKIP_SECONDS_MIN
import com.nuvio.app.features.player.PlayerSkipSecondsOptions
import nuvio.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource

/**
 * Skip step picker: one-tap presets plus a field for any whole number of seconds
 * between [PLAYER_SKIP_SECONDS_MIN] and [PLAYER_SKIP_SECONDS_MAX].
 */
@Composable
internal fun SkipSecondsInputDialog(
    title: String,
    selectedSeconds: Int,
    onSelected: (Int) -> Unit,
    onDismiss: () -> Unit,
) {
    var input by remember { mutableStateOf(selectedSeconds.toString()) }
    val parsed = input.toIntOrNull()
    val isValid = parsed != null && parsed in PLAYER_SKIP_SECONDS_MIN..PLAYER_SKIP_SECONDS_MAX

    DialogSurface(
        onDismissRequest = onDismiss,
        title = title,
        message = stringResource(Res.string.settings_playback_skip_description),
    ) {
        Column(
            // Shrinks and scrolls on short screens (phone landscape) so the buttons stay visible.
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f, fill = false)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            PlayerSkipSecondsOptions.forEach { seconds ->
                DialogOption(
                    text = stringResource(Res.string.settings_playback_skip_seconds_value, seconds),
                    selected = seconds == parsed,
                    onClick = { onSelected(seconds) },
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text(
                    text = stringResource(Res.string.settings_playback_skip_custom_input),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(1f),
                )
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    border = BorderStroke(
                        1.dp,
                        if (isValid) {
                            MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                        } else {
                            MaterialTheme.colorScheme.error
                        },
                    ),
                ) {
                    BasicTextField(
                        value = input,
                        onValueChange = { text -> input = text.filter(Char::isDigit).take(3) },
                        modifier = Modifier
                            .width(80.dp)
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        textStyle = MaterialTheme.typography.bodyLarge.copy(
                            color = MaterialTheme.colorScheme.onSurface,
                        ),
                        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                    )
                }
                Text(
                    text = stringResource(Res.string.settings_playback_skip_custom_unit),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            if (!isValid) {
                Text(
                    text = stringResource(
                        Res.string.settings_playback_skip_custom_range,
                        PLAYER_SKIP_SECONDS_MIN,
                        PLAYER_SKIP_SECONDS_MAX,
                    ),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.error,
                )
            }
        }
        DialogButtons {
            DialogButton(
                text = stringResource(Res.string.action_cancel),
                onClick = onDismiss,
            )
            DialogButton(
                text = stringResource(Res.string.action_save),
                onClick = { parsed?.takeIf { isValid }?.let(onSelected) },
                style = DialogButtonStyle.Primary,
                enabled = isValid,
            )
        }
    }
}
