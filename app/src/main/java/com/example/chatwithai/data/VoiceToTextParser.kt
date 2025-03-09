package com.example.chatwithai.data

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import com.example.chatwithai.presentation.chat.VoiceToTextParserState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class VoiceToTextParser @Inject constructor(
    private val app: Application
): RecognitionListener {

    private val _state = MutableStateFlow(VoiceToTextParserState())
    val state: StateFlow<VoiceToTextParserState> = _state

    val recognizer = SpeechRecognizer.createSpeechRecognizer(app)

    fun startListening(languageCode: String) {
        _state.update { VoiceToTextParserState() }

        if (!SpeechRecognizer.isRecognitionAvailable(app)) {
            _state.update {
                it.copy(
                    error = "Recognition is not available"
                )
            }
            Log.d("Speaking", "Recognition is not available")
        }

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, languageCode)
        }

        recognizer.setRecognitionListener(this)
        recognizer.startListening(intent)

        _state.update {
            it.copy(
                isSpeaking = true
            )
        }
    }

    fun stopListening() {
        _state.update {
            it.copy(
                isSpeaking = false
            )
        }
        recognizer.stopListening()
    }

    override fun onReadyForSpeech(p0: Bundle?) {
        _state.update {
            it.copy(
                error = null
            )
        }
    }

    override fun onEndOfSpeech() {
        _state.update {
            it.copy(
                isSpeaking = false
            )
        }
    }

    override fun onError(error: Int) {
        if (error == SpeechRecognizer.ERROR_CLIENT) {
            return
        }
        _state.update {
            it.copy(
                error = "Error: $error"
            )
        }
        when (error) {
            SpeechRecognizer.ERROR_AUDIO -> Log.e("VoiceToTextParser", "Audio recording error")
            SpeechRecognizer.ERROR_CLIENT -> Log.e("VoiceToTextParser", "Client side error")
            SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> Log.e("VoiceToTextParser", "Insufficient permissions")
            SpeechRecognizer.ERROR_NETWORK -> Log.e("VoiceToTextParser", "Network error")
            SpeechRecognizer.ERROR_NO_MATCH -> Log.e("VoiceToTextParser", "No match found")
            SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> Log.e("VoiceToTextParser", "Recognition service busy")
            SpeechRecognizer.ERROR_SERVER -> Log.e("VoiceToTextParser", "Server error")
            SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> Log.e("VoiceToTextParser", "Speech timeout")
            SpeechRecognizer.ERROR_LANGUAGE_NOT_SUPPORTED -> Log.e("VoiceToTextParser", "Language not supported")
            SpeechRecognizer.ERROR_LANGUAGE_UNAVAILABLE -> Log.e("VoiceToTextParser", "Language unavailable")
            else -> Log.e("VoiceToTextParser", "Unknown error code: $error")
        }
    }

    override fun onResults(results: Bundle?) {
        results
            ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            ?.getOrNull(0)
            ?.let { result ->
                _state.update {
                    it.copy(
                        spokenText = result
                    )
                }
                Log.d("Speaking", "Result: $result")
            }
    }

    fun clearSpokenText() {
        _state.update {
            it.copy(
                spokenText = ""
            )
        }
    }

    override fun onPartialResults(p0: Bundle?) = Unit

    override fun onEvent(p0: Int, p1: Bundle?) = Unit

    override fun onBeginningOfSpeech() = Unit

    override fun onRmsChanged(p0: Float) = Unit

    override fun onBufferReceived(p0: ByteArray?) = Unit
}

