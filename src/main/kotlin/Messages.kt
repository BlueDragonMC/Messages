import com.bluedragonmc.messagingsystem.message.Message
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.PolymorphicModuleBuilder
import kotlinx.serialization.modules.subclass
import java.util.*

val polymorphicModuleBuilder: PolymorphicModuleBuilder<Message>.() -> Unit = {
    subclass(PingMessage::class)
    subclass(SendChatMessage::class)
    subclass(RequestAddToQueueMessage::class)
    subclass(RequestRemoveFromQueueMessage::class)
    subclass(RequestCreateInstanceMessage::class)
    subclass(NotifyInstanceCreatedMessage::class)
    subclass(PlayJukeboxSongMessage::class)
    subclass(PauseJukeboxSongMessage::class)
    subclass(AddJukeboxSongToQueueMessage::class)
    subclass(RemoveJukeboxSongFromQueueMessage::class)
    subclass(GetJukeboxQueueMessage::class)
    subclass(JukeboxQueueResponseMessage::class)
    subclass(InvitePlayerToPartyMessage::class)
    subclass(AcceptPartyInvitationMessage::class)
    subclass(RemovePlayerFromPartyMessage::class)
    subclass(PartyChatMessage::class)
    subclass(SendFriendRequestMessage::class)
    subclass(AcceptFriendRequestMessage::class)
    subclass(RemoveFriendMessage::class)
    subclass(RequestFriendListMessage::class)
    subclass(FriendListResponseMessage::class)
}

// Helper data classes
@Serializable
data class GameType(val name: String, val mode: String? = null, val mapName: String? = null) : Message

enum class ChatType {
    CHAT, ACTION_BAR, TITLE, SUBTITLE, SOUND
}

// General
/**
 * Minestom => Service
 */
@Serializable
data class PingMessage(@Contextual val containerId: UUID, val versionInfo: Map<String, String>) : Message

/**
 * Service => Minestom
 */
@Serializable
data class SendChatMessage(@Contextual val targetPlayer: UUID, val message: String, val type: ChatType = ChatType.CHAT) : Message

/**
 * Minestom => Service
 */
@Serializable
data class RequestAddToQueueMessage(@Contextual val player: UUID, val gameType: GameType) : Message

/**
 * Minestom => Service
 */
@Serializable
data class RequestRemoveFromQueueMessage(@Contextual val player: UUID) : Message

/**
 * Service => Minestom
 */
@Serializable
data class RequestCreateInstanceMessage(@Contextual val containerId: UUID, val gameType: GameType) : Message

/**
 * Minestom => Service
 */
@Serializable
data class NotifyInstanceCreatedMessage(
    @Contextual val containerId: UUID, @Contextual val instanceId: UUID, val gameType: GameType
) : Message

/**
 * Minestom => Service
 */
@Serializable
data class PlayJukeboxSongMessage(@Contextual val player: UUID) : Message

/**
 * Minestom => Service
 */
@Serializable
data class PauseJukeboxSongMessage(@Contextual val player: UUID) : Message

/**
 * Minestom => Service
 */
@Serializable
data class AddJukeboxSongToQueueMessage(@Contextual val player: UUID, val song: String) : Message

/**
 * Minestom => Service
 */
@Serializable
data class RemoveJukeboxSongFromQueueMessage(@Contextual val player: UUID, val index: Int) : Message

/**
 * Minestom => Service (RPC)
 * Corresponds to [JukeboxQueueResponseMessage]
 */
@Serializable
data class GetJukeboxQueueMessage(@Contextual val player: UUID) : Message

/**
 * Service => Minestom (RPC)
 * Corresponds to [GetJukeboxQueueMessage]
 */
@Serializable
data class JukeboxQueueResponseMessage(val queue: List<String>) : Message

@Serializable
data class InvitePlayerToPartyMessage(@Contextual val partyOwner: UUID, @Contextual val player: UUID) : Message

@Serializable
data class AcceptPartyInvitationMessage(@Contextual val partyOwner: UUID, @Contextual val player: UUID) : Message

@Serializable
data class RemovePlayerFromPartyMessage(@Contextual val partyOwner: UUID, @Contextual val player: UUID) : Message

@Serializable
data class PartyChatMessage(@Contextual val player: UUID, val message: String) : Message

// Friends

@Serializable
data class SendFriendRequestMessage(@Contextual val player: UUID, @Contextual val targetPlayer: UUID) : Message

@Serializable
data class AcceptFriendRequestMessage(@Contextual val requester: UUID, @Contextual val targetPlayer: UUID) : Message

@Serializable
data class RemoveFriendMessage(@Contextual val player: UUID, @Contextual val targetPlayer: UUID) : Message

@Serializable
data class RequestFriendListMessage(@Contextual val player: UUID) : Message

@Serializable
data class FriendListResponseMessage(@Contextual val player: UUID) : Message