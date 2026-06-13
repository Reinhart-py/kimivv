package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File

enum class AppNavigationState {
    SPLASH, ONBOARDING, LOGIN, MAIN
}

enum class MainTab {
    DASHBOARD, AGENTS, CAMPAIGNS, LIVE_CALL, AUTOMATION, ACCOUNT
}

enum class CallState {
    IDLE, CONNECTING, ACTIVE, HUNG_UP
}

class AetherViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AetherRepository(application)

    // Navigation and Tab States
    private val _navigationState = MutableStateFlow(AppNavigationState.SPLASH)
    val navigationState: StateFlow<AppNavigationState> = _navigationState.asStateFlow()

    private val _currentTab = MutableStateFlow(MainTab.DASHBOARD)
    val currentTab: StateFlow<MainTab> = _currentTab.asStateFlow()

    // Room Database Observables
    val contacts = repository.contacts
    val aiAgents = repository.aiAgents
    val campaigns = repository.campaigns
    val callHistory = repository.callHistory
    val automationRules = repository.automationRules

    // Root Capabilities (Luxury Security Diagnostics)
    private val _isDeviceRooted = MutableStateFlow(false)
    val isDeviceRooted: StateFlow<Boolean> = _isDeviceRooted.asStateFlow()

    private val _lowLevelAudioInjection = MutableStateFlow(false)
    val lowLevelAudioInjection: StateFlow<Boolean> = _lowLevelAudioInjection.asStateFlow()

    private val _telephonyBusTap = MutableStateFlow(false)
    val telephonyBusTap: StateFlow<Boolean> = _telephonyBusTap.asStateFlow()

    private val _terminalLogs = MutableStateFlow<List<String>>(listOf("Aether Kernel initialized.", "Diag: Checking superuser permissions..."))
    val terminalLogs: StateFlow<List<String>> = _terminalLogs.asStateFlow()

    // Active Simulated Live Call State Flow (High-fidelity interactive visualizer)
    private val _liveCallState = MutableStateFlow(CallState.IDLE)
    val liveCallState: StateFlow<CallState> = _liveCallState.asStateFlow()

    private val _liveCallName = MutableStateFlow("")
    val liveCallName: StateFlow<String> = _liveCallName.asStateFlow()

    private val _liveCallPhone = MutableStateFlow("")
    val liveCallPhone: StateFlow<String> = _liveCallPhone.asStateFlow()

    private val _liveCallTranscript = MutableStateFlow("")
    val liveCallTranscript: StateFlow<String> = _liveCallTranscript.asStateFlow()

    private val _isAgentSpeaking = MutableStateFlow(false)
    val isAgentSpeaking: StateFlow<Boolean> = _isAgentSpeaking.asStateFlow()

    // Selected Agent for Studio customization
    private val _selectedAgent = MutableStateFlow<AIAgent?>(null)
    val selectedAgent: StateFlow<AIAgent?> = _selectedAgent.asStateFlow()

    // Integrations State
    private val _hubspotActive = MutableStateFlow(true)
    val hubspotActive: StateFlow<Boolean> = _hubspotActive.asStateFlow()

    private val _salesforceActive = MutableStateFlow(false)
    val salesforceActive: StateFlow<Boolean> = _salesforceActive.asStateFlow()

    private val _zapierActive = MutableStateFlow(true)
    val zapierActive: StateFlow<Boolean> = _zapierActive.asStateFlow()

    // Billing subscription Info
    private val _saasTier = MutableStateFlow("Sapphire Sovereign Enterprise")
    val saasTier: StateFlow<String> = _saasTier.asStateFlow()

    init {
        checkRootPresence()
        // Auto-initialize selected audio/agent once loaded
        viewModelScope.launch {
            repository.aiAgents.collect { list ->
                if (list.isNotEmpty() && _selectedAgent.value == null) {
                    _selectedAgent.value = list.first()
                }
            }
        }
    }

    private fun checkRootPresence() {
        viewModelScope.launch(Dispatchers.IO) {
            val paths = arrayOf(
                "/system/app/Superuser.apk",
                "/sbin/su",
                "/system/bin/su",
                "/system/xbin/su",
                "/data/local/xbin/su",
                "/data/local/bin/su",
                "/system/sd/xbin/su",
                "/system/bin/failsafe/su",
                "/data/local/su"
            )
            var rooted = false
            for (path in paths) {
                if (File(path).exists()) {
                    rooted = true
                    break
                }
            }
            _isDeviceRooted.value = rooted
            appendTerminal(if (rooted) "System status: [ROOT_PRIVILEGES_GRANTED]. Direct bus capture enabled." else "System status: [NON_ROOT_SANDBOX]. Running on pure userland high-performance SIP virtualizer.")
        }
    }

    fun appendTerminal(msg: String) {
        val currentLogs = _terminalLogs.value.toMutableList()
        currentLogs.add("Aether@Client:~$ $msg")
        if (currentLogs.size > 20) currentLogs.removeAt(0)
        _terminalLogs.value = currentLogs
    }

    // --- Actions ---

    fun setNavigationState(state: AppNavigationState) {
        _navigationState.value = state
    }

    fun selectTab(tab: MainTab) {
        _currentTab.value = tab
    }

    fun setSelectedAgent(agent: AIAgent) {
        _selectedAgent.value = agent
    }

    fun toggleLowLevelAudio() {
        if (_isDeviceRooted.value) {
            _lowLevelAudioInjection.value = !_lowLevelAudioInjection.value
            appendTerminal("AUDIO_INJECT: State set to ${_lowLevelAudioInjection.value}")
        } else {
            appendTerminal("ERR: Require root access privilege to latch directly into system mixer.")
        }
    }

    fun toggleTelephonyBusTap() {
        if (_isDeviceRooted.value) {
            _telephonyBusTap.value = !_telephonyBusTap.value
            appendTerminal("BUS_TAP: Kernel capture interface set to ${_telephonyBusTap.value}")
        } else {
            appendTerminal("ERR: Kernel bypass access is only granted on Root-enabled custom operating systems.")
        }
    }

    fun toggleHubspot() { _hubspotActive.value = !_hubspotActive.value }
    fun toggleSalesforce() { _salesforceActive.value = !_salesforceActive.value }
    fun toggleZapier() { _zapierActive.value = !_zapierActive.value }
    fun setSaasTier(tier: String) { _saasTier.value = tier }

    // --- DB Wrappers ---
    fun addContact(name: String, phone: String, status: String, tags: String) {
        viewModelScope.launch {
            repository.addContact(
                Contact(name = name, phone = phone, status = status, tags = tags, lastContacted = "Just created")
            )
        }
    }

    fun addAgent(name: String, voice: String, systemPrompt: String) {
        viewModelScope.launch {
            repository.addAgent(
                AIAgent(name = name, voiceType = voice, systemPrompt = systemPrompt)
            )
        }
    }

    fun addCampaign(name: String, agentId: Int, total: Int) {
        viewModelScope.launch {
            repository.addCampaign(
                Campaign(name = name, agentId = agentId, status = "Active", totalContacts = total, completedContacts = 0, successRate = 100.0f, scheduleTime = System.currentTimeMillis())
            )
        }
    }

    fun addAutomationRule(trigger: String, action: String) {
        viewModelScope.launch {
            repository.addAutomationRule(
                AutomationRule(triggerEvent = trigger, actionToTake = action, isEnabled = true)
            )
        }
    }

    fun clearAllLogs() {
        viewModelScope.launch {
            repository.clearHistory()
        }
    }

    // --- Live call simulator character-by-character generator ---
    fun initiateSimulatedCall(name: String, phone: String) {
        viewModelScope.launch {
            _liveCallState.value = CallState.CONNECTING
            _liveCallName.value = name
            _liveCallPhone.value = phone
            _liveCallTranscript.value = ""

            // Elegant loading delays mimicking SIP / PSTN Trunk registrations
            delay(1800)
            _liveCallState.value = CallState.ACTIVE

            val lines = listOf(
                "System Agent" to "Connecting downstream call to secure recipient $name on PSTN Trunk...",
                name to "Hello? Who is this?",
                "System Agent" to "Good evening, $name. I am calling from the Aether Intelligent Asset trust on behalf of the management concierge. I hope your evening is proceeding seamlessly.",
                name to "Oh, yes! I was expecting details on the custom integration options. Is this the autonomous platform?",
                "System Agent" to "Indeed. I am Aether's dedicated communications representative node. I can automatically register and build your bespoke voice nodes. Should we lock in tomorrow at 10 AM to sync your database metrics?",
                name to "Absolutely. 10 AM works wonders. Send over the confirmation card.",
                "System Agent" to "Marvelous. I have securely generated your slot credentials. Welcome to the elite communications standard."
            )

            for ((speaker, text) in lines) {
                if (_liveCallState.value != CallState.ACTIVE) break
                val speakerLabel = if (speaker == "System Agent") "AI Agent" else speaker
                _liveCallTranscript.value += "\n\n[$speakerLabel]: "

                _isAgentSpeaking.value = (speaker == "System Agent")

                // Print text smoothly, character by character
                for (char in text) {
                    if (_liveCallState.value != CallState.ACTIVE) break
                    _liveCallTranscript.value += char
                    delay(if (speaker == "System Agent") 30 else 55)
                }
                delay(1200)
            }

            _isAgentSpeaking.value = false
            if (_liveCallState.value == CallState.ACTIVE) {
                _liveCallState.value = CallState.HUNG_UP
                // Save this completed transaction to local database!
                repository.addCall(
                    CallHistory(
                        contactName = name,
                        phoneNumber = phone,
                        direction = "Outbound",
                        durationSeconds = 64,
                        status = "Completed",
                        transcript = _liveCallTranscript.value,
                        sentiment = "Positive",
                        timestamp = System.currentTimeMillis()
                    )
                )
            }
        }
    }

    fun hangUpCall() {
        _liveCallState.value = CallState.HUNG_UP
        _isAgentSpeaking.value = false
        viewModelScope.launch {
            delay(1000)
            _liveCallState.value = CallState.IDLE
        }
    }
}
