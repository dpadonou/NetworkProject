package fr.istic.mob.networkDP

import kotlinx.serialization.Serializable

@Serializable
enum class States {
    ADDING_NODE,
    ADDING_CONNEXION,
    READING_MODE,
    UPDATE_MODE,
    RESET,
    SAVE,
    IMPORT_NETWORK,
    IMPORT_PLAN,
    SEND_NETWORK
}