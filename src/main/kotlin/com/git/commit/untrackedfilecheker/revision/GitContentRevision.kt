package com.git.commit.untrackedfilecheker.revision

import com.intellij.openapi.vcs.FilePath
import com.intellij.openapi.vcs.changes.ContentRevision
import com.intellij.openapi.vcs.history.VcsRevisionNumber

/**
 * Represents a content revision for untracked files in a Git repository.
 *
 * This class implements the ContentRevision interface and is used to encapsulate the
 * details of an untracked file, specifically its file path. Since the file is untracked,
 * it does not have a version history, which is reflected in the methods provided by this
 * class. The `getRevisionNumber` method returns a null revision number to indicate the
 * absence of a versioned history, while the `getContent` method returns an empty string
 * since there is no committed content available for untracked files.
 *
 * @property filePath The FilePath representing the location of the untracked file.
 */
class GitContentRevision(private val filePath: FilePath) : ContentRevision {

    // Constructor initializes the GitContentRevision with the provided FilePath.
    // This will represent the untracked file in the version control system.

    override fun getFile(): FilePath {
        // Returns the FilePath associated with this content revision.
        // This is essential for identifying which file the revision corresponds to.
        return filePath
    }

    override fun getRevisionNumber(): VcsRevisionNumber {
        // Returns a null revision number as this content revision represents an untracked file.
        // Untracked files do not have a version history, so we use VcsRevisionNumber.NULL to indicate this absence.
        return VcsRevisionNumber.NULL
    }

    override fun getContent(): String {
        // Returns an empty string as the content for this revision.
        // Since the file is untracked, there is no committed content to retrieve.
        // An empty string indicates that no versioned content is available.
        return ""
    }
}
