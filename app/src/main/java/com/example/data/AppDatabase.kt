package com.example.data

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

// --- ENTITIES ---

@Entity(tableName = "contacts")
data class Contact(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val phone: String,
    val status: String, // "Lead", "Qualified", "Booked", "No Answer"
    val tags: String,   // Comma separated e.g. "Enterprise, High-Intent"
    val lastContacted: String
)

@Entity(tableName = "ai_agents")
data class AIAgent(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val voiceType: String, // "Kimi Male (Natural)", "Kimi Female (Warm)", "Kimi Outbound (Standard)"
    val pitch: Float = 1.0f,
    val speed: Float = 1.0f,
    val systemPrompt: String,
    val temperature: Float = 0.7f,
    val language: String = "English (US)"
)

@Entity(tableName = "campaigns")
data class Campaign(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val agentId: Int,
    val status: String, // "Active", "Paused", "Scheduled", "Completed"
    val totalContacts: Int,
    val completedContacts: Int,
    val successRate: Float, // percentage e.g. 84.5
    val scheduleTime: Long
)

@Entity(tableName = "call_history")
data class CallHistory(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val contactName: String,
    val phoneNumber: String,
    val direction: String, // "Inbound", "Outbound"
    val durationSeconds: Int,
    val status: String,    // "Completed", "Busy", "No Answer", "Active"
    val transcript: String, // Conversational transcript
    val sentiment: String,  // "Positive", "Neutral", "Negative"
    val timestamp: Long
)

@Entity(tableName = "automation_rules")
data class AutomationRule(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val triggerEvent: String,
    val actionToTake: String,
    val isEnabled: Boolean = true
)

// --- DAOs ---

@Dao
interface ContactDao {
    @Query("SELECT * FROM contacts ORDER BY id DESC")
    fun getAllContacts(): Flow<List<Contact>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContact(contact: Contact)

    @Query("DELETE FROM contacts WHERE id = :id")
    suspend fun deleteContact(id: Int)

    @Query("DELETE FROM contacts")
    suspend fun deleteAllContacts()
}

@Dao
interface AIAgentDao {
    @Query("SELECT * FROM ai_agents ORDER BY id DESC")
    fun getAllAgents(): Flow<List<AIAgent>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAgent(agent: AIAgent)

    @Query("DELETE FROM ai_agents WHERE id = :id")
    suspend fun deleteAgent(id: Int)
}

@Dao
interface CampaignDao {
    @Query("SELECT * FROM campaigns ORDER BY id DESC")
    fun getAllCampaigns(): Flow<List<Campaign>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCampaign(campaign: Campaign)

    @Query("DELETE FROM campaigns WHERE id = :id")
    suspend fun deleteCampaign(id: Int)
}

@Dao
interface CallHistoryDao {
    @Query("SELECT * FROM call_history ORDER BY timestamp DESC")
    fun getAllCalls(): Flow<List<CallHistory>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCall(call: CallHistory)

    @Query("DELETE FROM call_history")
    suspend fun clearHistory()
}

@Dao
interface AutomationRuleDao {
    @Query("SELECT * FROM automation_rules ORDER BY id ASC")
    fun getAllRules(): Flow<List<AutomationRule>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRule(rule: AutomationRule)
}

// --- DATABASE ---

@Database(
    entities = [
        Contact::class,
        AIAgent::class,
        Campaign::class,
        CallHistory::class,
        AutomationRule::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun contactDao(): ContactDao
    abstract fun aiAgentDao(): AIAgentDao
    abstract fun campaignDao(): CampaignDao
    abstract fun callHistoryDao(): CallHistoryDao
    abstract fun automationRuleDao(): AutomationRuleDao
}
