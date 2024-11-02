package com.git.commit.untrackedfilecheker.utils

import com.git.commit.untrackedfilecheker.revision.GitContentRevision
import com.intellij.openapi.project.Project
import com.intellij.openapi.vcs.FilePath
import com.intellij.openapi.vcs.FileStatus
import com.intellij.openapi.vcs.VcsException
import com.intellij.openapi.vcs.changes.Change
import com.intellij.openapi.vcs.changes.ChangeListManager
import com.intellij.openapi.vcs.changes.LocalChangeList
import org.slf4j.LoggerFactory

/**
 * Utility object for Git-related operations in an IntelliJ project.
 *
 * Provides methods to retrieve untracked files and add them to version control.
 */
object GitUtils {
    private val logger = LoggerFactory.getLogger(GitUtils::class.java)

    /**
     * Retrieves a list of untracked files within the 'src' directory of the specified project.
     *
     * @param project The IntelliJ project to search for untracked files.
     * @return A list of FilePath representing untracked files in the 'src' directory.
     */
    fun getUntrackedFiles(project: Project): List<FilePath> {
        logger.info("Retrieving untracked files for project: ${project.name}")
        val changeListManager = ChangeListManager.getInstance(project)
        val untrackedFiles = changeListManager.unversionedFilesPaths
        logger.debug("Found ${untrackedFiles.size} untracked files in total.")

        val srcDirectory = "${project.basePath}/src"
        val srcUntrackedFiles = untrackedFiles.filter { filePath ->
            filePath.path.startsWith(srcDirectory)
        }
        logger.info("Found ${srcUntrackedFiles.size} untracked files in 'src' directory.")
        return srcUntrackedFiles
    }

    /**
     * Adds the specified files to the Git repository.
     *
     * @param project The IntelliJ project where the files will be added.
     * @param files A set of FilePath representing the files to be added.
     * @return True if the files were successfully added, false otherwise.
     */
    fun addFilesToGit(project: Project, files: Set<FilePath>): Boolean {
        logger.info("Adding files to Git in project: ${project.name}")
        logger.debug("Files to add: ${files.joinToString(", ") { it.path }}")

        val changeListManager = ChangeListManager.getInstance(project)
        val defaultChangeList: LocalChangeList = changeListManager.defaultChangeList
        val changesToCommit = mutableListOf<Change>()

        for (filePath in files) {
            logger.debug("Adding revision for file: ${filePath.path}")
            val afterRevision = GitContentRevision(filePath)
            val change = Change(null, afterRevision, FileStatus.ADDED)
            changesToCommit.add(change)
        }

        return try {
            logger.info("Attempting to commit ${changesToCommit.size} changes to the default changelist.")
            changeListManager.commitChanges(defaultChangeList, changesToCommit)
            logger.info("Successfully added files to Git.")
            true
        } catch (e: VcsException) {
            logger.error("Failed to add files to Git: ${e.message}", e)
            false
        }
    }
}