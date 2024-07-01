package com.emailmarketingtool.enums

enum class RoleHolder(
    val roleType: Role,
    val permissions: Set<String>
) {
    ADMIN(Role.ADMIN,adminPermissions),
    USER(Role.USER, userPermissions)
}

private val userPermissions = setOf(
    "user.update.password",
    "user.update",
    "campaign.create",
    "campaign.update",
    "campaign.view",
    "contact.group.view",
    "campaign.delete",
    "contact.create",
    "contact.update",
    "contact.view",
    "contact.delete",
    "contact.upload",
    "template.create",
    "template.update",
    "template.delete",
    "template.view",
    "sender.create",
    "sender.delete",
    "sender.update",
    "sender.view",
    )

private val adminPermissions = setOf<String>().plus(userPermissions)