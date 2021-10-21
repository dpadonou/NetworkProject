package fr.istic.mob.networkdp

import kotlinx.serialization.Serializable

@Serializable
enum class States {
    ADDING_NODE,
    ADDING_CONNEXION,
    READING_MODE,
    UPDATE_MODE,
    RESET
}