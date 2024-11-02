package com.git.commit.untrackedfilecheker.notification

import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project
import com.intellij.openapi.vcs.FilePath

object NotificationService {
    fun showUntrackedFilesNotification(project: Project, untrackedFiles: List<FilePath>) {
        val message = "Untracked files detected in src folder:\n" + untrackedFiles.joinToString("\n")

        val notificationGroup = NotificationGroupManager.getInstance()
            .getNotificationGroup("Git Commit Completeness Checker")

        val notification = notificationGroup.createNotification(
            "Untracked Files",
            message,
            NotificationType.WARNING
        )

        notification.notify(project)
    }
}