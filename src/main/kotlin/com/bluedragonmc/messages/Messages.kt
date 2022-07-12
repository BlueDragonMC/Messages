package com.bluedragonmc.messages

import com.bluedragonmc.messagingsystem.message.Message
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.PolymorphicModuleBuilder
import kotlinx.serialization.modules.subclass
import java.util.*

val polymorphicModuleBuilder: PolymorphicModuleBuilder<Message>.() -> Unit = {
    subclass(PingMessage::class)
    subclass(GameStateUpdateMessage::class)
    subclass(SendChatMessage::class)
    subclass(RequestAddToQueueMessage::class)
    subclass(RequestRemoveFromQueueMessage::class)
    subclass(RequestCreateInstanceMessage::class)
    subclass(NotifyInstanceCreatedMessage::class)
    subclass(NotifyInstanceRemovedMessage::class)
    subclass(SendPlayerToInstanceMessage::class)
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
/**
 * Stores the name, game mode, and map of a game.
 */
@Serializable
data class GameType(val name: String, val mode: String? = null, val mapName: String? = null) : Message

enum class ChatType {
    CHAT, ACTION_BAR, TITLE, SUBTITLE, SOUND
}

// General
/**
 * Sent to the Puffin service when a Minestom server starts up.
 * @param containerId A unique identifier for the Minestom server and its Docker container.
 * @param versionInfo Currently unused.
 */
@Serializable
data class PingMessage(@Contextual val containerId: UUID, val versionInfo: Map<String, String>) : Message

/**
 * Sent to the Puffin service when the state of a game changes.
 * The "state" of a game decides whether or not players can join, and how many can join.
 * Events like the game starting or a new player joining can change the number of players allowed to join.
 * When those things happen, this message is sent to the service for use with the queue system.
 * @param instanceId A unique identifier for the Minestom server and its Docker container.
 * @param emptyPlayerSlots How many more players are allowed to join.
 */
@Serializable
data class GameStateUpdateMessage(@Contextual val instanceId: UUID, val emptyPlayerSlots: Int) : Message

/**
 * Sent to the Minestom server to send a message to a player.
 * This can be a chat message, action bar, title, subtitle, or sound.
 * @param targetPlayer The UUID of the receiver of the message.
 * @param message The contents of the message. This will be serialized with MiniMessage.
 * @type The type of message to send.
 */
@Serializable
data class SendChatMessage(
    @Contextual val targetPlayer: UUID, val message: String, @SerialName("chatType") val type: ChatType = ChatType.CHAT
) : Message

/**
 * Sent to the Puffin service to request that a player is added to the queue.
 * @param player The UUID of the player to add to the queue.
 * @param gameType Information about the game to queue the player for. Includes the name, mode, and map the player wants to queue for.
 */
@Serializable
data class RequestAddToQueueMessage(@Contextual val player: UUID, val gameType: GameType) : Message

/**
 * Sent to the Puffin service to request that a player is removed from the queue.
 * @param player The UUID of the player to remove from the queue.
 */
@Serializable
data class RequestRemoveFromQueueMessage(@Contextual val player: UUID) : Message

/**
 * Sent to a Minestom server to request it to create a new instance.
 * @param containerId A unique identifier for the Minestom server and its Docker container.
 * @param gameType Information about the specific game to start. Includes the name, mode, and map of the game.
 */
@Serializable
data class RequestCreateInstanceMessage(@Contextual val containerId: UUID, val gameType: GameType) : Message

/**
 * Sent to the Puffin service when a minigame instance has been created and all modules are initialized.
 * @param containerId A unique identifier for the Minestom server and its docker container.
 * @param instanceId The UUID of the newly created Minestom instance.
 * @param gameType Information about the specific game that was created. Includes the name, mode, and map of the game.
 */
@Serializable
data class NotifyInstanceCreatedMessage(
    @Contextual val containerId: UUID, @Contextual val instanceId: UUID, val gameType: GameType
) : Message

/**
 * Sent to the Puffin service when a minigame instance is removed from a Minestom server. This is usually because the game ended.
 * @param containerId A unique identifier for the Minestom server and its docker container.
 * @param instanceId The UUID of the recently removed Minestom instance.
 */
@Serializable
data class NotifyInstanceRemovedMessage(@Contextual val containerId: UUID, @Contextual val instanceId: UUID) : Message

/**
 * Sent to a Minestom server when a player should be sent to a specific instance.
 * @param player The UUID of the player.
 * @param instance The UUID of the instance.
 */
@Serializable
data class SendPlayerToInstanceMessage(@Contextual val player: UUID, @Contextual val instance: UUID) : Message

/**
 * Sent to the Puffin service when a player wants to resume their jukebox song.
 * @param player The UUID of the player.
 */
@Serializable
data class PlayJukeboxSongMessage(@Contextual val player: UUID) : Message

/**
 * Sent to the Puffin service when a player wants to pause their jukebox song.
 * @param player The UUID of the player.
 */
@Serializable
data class PauseJukeboxSongMessage(@Contextual val player: UUID) : Message

/**
 * Sent to the Puffin service when a player wants to add a jukebox song to their song queue.
 * @param player The UUID of the player.
 * @param song The name of the song to add to the queue.
 */
@Serializable
data class AddJukeboxSongToQueueMessage(@Contextual val player: UUID, val song: String) : Message

/**
 * Sent to the Puffin service when a player wants to remove a jukebox song from their song queue.
 * @param player The UUID of the player.
 * @param index The index of the song to remove from the queue.
 */
@Serializable
data class RemoveJukeboxSongFromQueueMessage(@Contextual val player: UUID, val index: Int) : Message

/**
 * RPC message sent to the Puffin service to request a list of all songs in a player's queue.
 * Corresponds with [JukeboxQueueResponseMessage]
 * @param player The UUID of the player.
 */
@Serializable
data class GetJukeboxQueueMessage(@Contextual val player: UUID) : Message

/**
 * The response to [GetJukeboxQueueMessage] containing a list of all songs in the player's queue.
 * @param queue A list containing the names of all songs in the player's queue.
 */
@Serializable
data class JukeboxQueueResponseMessage(val queue: List<String>) : Message

/**
 * Sent to the Puffin service to request a player be invited to a party.
 * @param partyOwner The UUID of the player that owns the party.
 * @param player The UUID of the player being invited to the party.
 */
@Serializable
data class InvitePlayerToPartyMessage(@Contextual val partyOwner: UUID, @Contextual val player: UUID) : Message

/**
 * Sent to the Puffin service when a player accepts a party invitation.
 * @param partyOwner The UUID of the player that owns the party.
 * @param player The UUID of the player being invited to the party.
 */
@Serializable
data class AcceptPartyInvitationMessage(@Contextual val partyOwner: UUID, @Contextual val player: UUID) : Message

/**
 * Sent to the Puffin service when a player needs to be removed from the party.
 * A player needs to be removed when they either disconnected from the server or manually left the party.
 * @param partyOwner The UUID of the player that owns the party.
 * @param player The UUID of the player to be removed from the party.
 */
@Serializable
data class RemovePlayerFromPartyMessage(@Contextual val partyOwner: UUID, @Contextual val player: UUID) : Message

/**
 * Sent to the Puffin service when a player sends a message in party chat.
 * @param player The UUID of the player that sent the message.
 * @param message The contents of the chat message.
 */
@Serializable
data class PartyChatMessage(@Contextual val player: UUID, val message: String) : Message

/**
 * Sent to the Puffin service to transfer ownership of a party.
 * @param oldOwner The UUID of the previous owner of the party.
 * @param newOwner The UUID of the player to transfer ownership to.
 */
@Serializable
data class PartyTransferMessage(@Contextual val oldOwner: UUID, @Contextual val newOwner: UUID) : Message

/**
 * Sent to the Puffin service to send all party members to the same instance.
 * @param partyOwner The UUID of the player that owns the party.
 * @param containerId A unique identifier for the Minestom server and its docker container. All party members will be sent to an instance on this server.
 * @param instanceId The UUID of the instance to send all party members to.
 */
@Serializable
data class PartyWarpMessage(@Contextual val partyOwner: UUID, @Contextual val containerId: UUID, @Contextual val instanceId: UUID) : Message

// Friends

/**
 * Sent to the Puffin service when a player sends a friend request to another player.
 * @param player The UUID of the sender of the friend request.
 * @param targetPlayer The UUID of the receiver of the friend request.
 */
@Serializable
data class SendFriendRequestMessage(@Contextual val player: UUID, @Contextual val targetPlayer: UUID) : Message

/**
 * Sent to the Puffin service when a player accepts a friend request from another player.
 * @param requester The UUID of the sender of the friend request.
 * @param targetPlayer The UUID of the receiver/acceptor of the friend request.
 */
@Serializable
data class AcceptFriendRequestMessage(@Contextual val requester: UUID, @Contextual val targetPlayer: UUID) : Message

/**
 * Sent to the Puffin service when a player removes a friend from their friends list.
 * @param player The UUID of the player that removed a friend.
 * @param targetPlayer The UUID of the friend that was removed.
 */
@Serializable
data class RemoveFriendMessage(@Contextual val player: UUID, @Contextual val targetPlayer: UUID) : Message

/**
 * Sent to the Puffin service to request all of a player's friends.
 * Corresponds with [FriendListResponseMessage]
 * @param player The UUID of the player.
 */
@Serializable
data class RequestFriendListMessage(@Contextual val player: UUID) : Message

/**
 * The response to [RequestFriendListMessage] containing a list of all the player's friends.
 * @param friends A map of containing all the player's friends.
 * The key is the UUID of the friend, and the value is the game the friend is currently in, or null if they are offline.
 */
@Serializable
data class FriendListResponseMessage(@Contextual val friends: Map<@Contextual UUID, GameType?>) : Message