package com.example.data

import android.content.Context
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class KimiRepository(private val context: Context) {

    val database: AppDatabase by lazy {
        Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "kimi_call_db"
        )
        .fallbackToDestructiveMigration()
        .build()
    }

    private val contactDao = database.contactDao()
    private val aiAgentDao = database.aiAgentDao()
    private val campaignDao = database.campaignDao()
    private val callHistoryDao = database.callHistoryDao()
    private val automationRuleDao = database.automationRuleDao()

    // --- Flows ---
    val contacts: Flow<List<Contact>> = contactDao.getAllContacts()
    val aiAgents: Flow<List<AIAgent>> = aiAgentDao.getAllAgents()
    val campaigns: Flow<List<Campaign>> = campaignDao.getAllCampaigns()
    val callHistory: Flow<List<CallHistory>> = callHistoryDao.getAllCalls()
    val automationRules: Flow<List<AutomationRule>> = automationRuleDao.getAllRules()

    init {
        // Preseed if database is empty on background dispatcher
        CoroutineScope(Dispatchers.IO).launch {
            if (aiAgentDao.getAllAgents().first().isEmpty()) {
                preseedDatabase()
            }
        }
    }

    private suspend fun preseedDatabase() = withContext(Dispatchers.IO) {
        // 1. Initial High-Performance Calling Agents
        val agents = listOf(
            AIAgent(
                name = "Kimi Pro (Enterprise)",
                voiceType = "Kimi Male (Natural)",
                pitch = 1.05f,
                speed = 0.98f,
                systemPrompt = "You are Kimi Pro, an executive call assistant. Speak with composure, clarity, and warm reassurance. Guide clients through onboarding.",
                temperature = 0.65f,
                language = "English (US)"
            ),
            AIAgent(
                name = "Kimi Care (Warm)",
                voiceType = "Kimi Female (Warm)",
                pitch = 1.15f,
                speed = 1.05f,
                systemPrompt = "You are Kimi Care, a warm client relations representative. Provide a helpful booking experience and answer queries with extreme helpfulness.",
                temperature = 0.70f,
                language = "English (US)"
            ),
            AIAgent(
                name = "Kimi Lead (Standard)",
                voiceType = "Kimi Outbound (Standard)",
                pitch = 0.90f,
                speed = 1.00f,
                systemPrompt = "You are Kimi Lead, an outbound communication assistant. Focus on client questions, schedule planning, and natural responses.",
                temperature = 0.75f,
                language = "English (UK)"
            )
        )
        agents.forEach { aiAgentDao.insertAgent(it) }

        // 2. High-converting Contacts
        val preseededContacts = listOf(
            Contact(name = "Marcus Aurelius", phone = "+1 (555) 019-2831", status = "Booked", tags = "Enterprise, High-Intent", lastContacted = "10 mins ago"),
            Contact(name = "Amara Sterling", phone = "+1 (555) 782-9901", status = "Qualified", tags = "VIP Member, Tech CEO", lastContacted = "2 hours ago"),
            Contact(name = "Julian Vance", phone = "+1 (555) 438-1290", status = "Lead", tags = "SaaS Founder", lastContacted = "1 day ago"),
            Contact(name = "Elena Rostova", phone = "+1 (555) 293-8471", status = "No Answer", tags = "Cold Prospect", lastContacted = "Just now"),
            Contact(name = "Dorian Grey", phone = "+1 (555) 901-2384", status = "Qualified", tags = "Venture Partner", lastContacted = "Yesterday")
        )
        preseededContacts.forEach { contactDao.insertContact(it) }

        // 3. Automation Campaigns
        val campaignsList = listOf(
            Campaign(name = "Q3 Enterprise Outbound Rollout", agentId = 1, status = "Active", totalContacts = 120, completedContacts = 84, successRate = 88.5f, scheduleTime = System.currentTimeMillis()),
            Campaign(name = "SaaS Concierge Inbound Booking", agentId = 2, status = "Active", totalContacts = 50, completedContacts = 39, successRate = 92.0f, scheduleTime = System.currentTimeMillis()),
            Campaign(name = "Cold Lead Nurturing Blitz", agentId = 3, status = "Paused", totalContacts = 250, completedContacts = 45, successRate = 42.1f, scheduleTime = System.currentTimeMillis())
        )
        campaignsList.forEach { campaignDao.insertCampaign(it) }

        // 4. Historical Calls with clean transcripts
        val calls = listOf(
            CallHistory(
                contactName = "Marcus Aurelius",
                phoneNumber = "+1 (555) 019-2831",
                direction = "Outbound",
                durationSeconds = 142,
                status = "Completed",
                transcript = "Agent: Good afternoon Marcus. I hope your day is going exceptionally well. I am calling from Kimi Calling Systems.\nMarcus: Hello. Yes, I was actually looking at your client dashboard. It looks clean.\nAgent: Thank you. Our technology is designed to orchestrate seamless customer communications. I can book an onboarding slot for you tomorrow at 3 PM.\nMarcus: Great, that works perfectly. Please send the confirmation details.\nAgent: Configured. The details have been sent to your device.",
                sentiment = "Positive",
                timestamp = System.currentTimeMillis() - 600000
            ),
            CallHistory(
                contactName = "Amara Sterling",
                phoneNumber = "+1 (555) 782-9901",
                direction = "Inbound",
                durationSeconds = 210,
                status = "Completed",
                transcript = "Amara: Hi! I noticed Kimi Call handled my client's query instantly. I'd love to set up this automated answering system for my consultancy.\nAgent: Welcome, Ms. Amara. I would be delighted to set this up for you. May I confirm your primary business hours?\nAmara: Yes, 24/7 global coverage is preferred.\nAgent: Excellent. It is now active. We are initiating the pilot program.",
                sentiment = "Positive",
                timestamp = System.currentTimeMillis() - 3600000
            ),
            CallHistory(
                contactName = "Elena Rostova",
                phoneNumber = "+1 (555) 293-8471",
                direction = "Outbound",
                durationSeconds = 0,
                status = "No Answer",
                transcript = "[Call placed - Ringing... No reply received. Custom voicemail dropped.]",
                sentiment = "Neutral",
                timestamp = System.currentTimeMillis() - 7200000
            )
        )
        calls.forEach { callHistoryDao.insertCall(it) }

        // 5. Automation Rules
        val rules = listOf(
            AutomationRule(triggerEvent = "On High-Intent Lead Qualified", actionToTake = "Trigger real-time booking sequence via Kimi Pro"),
            AutomationRule(triggerEvent = "On Call Unanswered", actionToTake = "Drop 'Concierge Soft' voice note after 24 hours"),
            AutomationRule(triggerEvent = "On Inbound Call Busy Hour", actionToTake = "Route overflow traffic to Secondary Kimi Care"),
            AutomationRule(triggerEvent = "On Appointment Completed", actionToTake = "Syndicate contact record to CRM integrations")
        )
        rules.forEach { automationRuleDao.insertRule(it) }
    }

    // --- Direct DB Actions ---
    suspend fun addContact(contact: Contact) = withContext(Dispatchers.IO) {
        contactDao.insertContact(contact)
    }

    suspend fun deleteContact(id: Int) = withContext(Dispatchers.IO) {
        contactDao.deleteContact(id)
    }

    suspend fun addAgent(agent: AIAgent) = withContext(Dispatchers.IO) {
        aiAgentDao.insertAgent(agent)
    }

    suspend fun deleteAgent(id: Int) = withContext(Dispatchers.IO) {
        aiAgentDao.deleteAgent(id)
    }

    suspend fun addCampaign(campaign: Campaign) = withContext(Dispatchers.IO) {
        campaignDao.insertCampaign(campaign)
    }

    suspend fun addCall(call: CallHistory) = withContext(Dispatchers.IO) {
        callHistoryDao.insertCall(call)
    }

    suspend fun addAutomationRule(rule: AutomationRule) = withContext(Dispatchers.IO) {
        automationRuleDao.insertRule(rule)
    }

    suspend fun clearHistory() = withContext(Dispatchers.IO) {
        callHistoryDao.clearHistory()
    }
}
