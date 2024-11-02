package com.git.commit.untrackedfilecheker.listener

import com.git.commit.untrackedfilecheker.panel.UntrackedFilesDialog
import com.git.commit.untrackedfilecheker.utils.GitUtils
import com.intellij.openapi.vcs.CheckinProjectPanel
import com.intellij.openapi.vcs.changes.CommitContext
import com.intellij.openapi.vcs.checkin.CheckinHandler
import com.intellij.openapi.vcs.checkin.CheckinHandlerFactory
import org.slf4j.LoggerFactory

/**
 * `GitCommitListener` is a custom implementation of `CheckinHandlerFactory` that intercepts
 * the commit process in IntelliJ IDEA. This listener detects any untracked files in the Git
 * repository before a commit, prompts the user to either add these files to the commit or
 * ignore them, and then proceeds based on the user's selection.
 */
class GitCommitListener : CheckinHandlerFactory() {

    private val logger = LoggerFactory.getLogger(GitCommitListener::class.java)

    override fun createHandler(panel: CheckinProjectPanel, commitContext: CommitContext): CheckinHandler {
        logger.info("Creating handler for commit check")

        return object : CheckinHandler() {
            override fun beforeCheckin(): ReturnResult {
                val project = panel.project
                val untrackedFilePaths = GitUtils.getUntrackedFiles(project)

                logger.info("Checking for untracked files before commit")
                if (untrackedFilePaths.isNotEmpty()) {
                    logger.info("Found ${untrackedFilePaths.size} untracked files")

                    val dialog = UntrackedFilesDialog(project, untrackedFilePaths)
                    if (dialog.showAndGet()) {
                        val selectedFiles = dialog.getSelectedFiles()
                        logger.info("User selected ${selectedFiles.size} files to add to the commit")

                        if (selectedFiles.isNotEmpty()) {
                            val addedSuccessfully = GitUtils.addFilesToGit(project, selectedFiles)
                            if (addedSuccessfully) {
                                logger.info("Successfully added selected files to Git")
                            } else {
                                logger.error("Failed to add selected files to Git")
                                return ReturnResult.CANCEL
                            }
                        }
                        return ReturnResult.COMMIT
                    } else {
                        logger.info("User canceled the dialog; commit will be canceled")
                        return ReturnResult.CANCEL
                    }
                }

                logger.info("No untracked files found; proceeding with commit")
                return ReturnResult.COMMIT
            }
        }
    }
}