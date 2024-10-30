package com.git.commit.commitchecker.panel

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.vcs.FilePath
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.components.JBPanel
import com.intellij.ui.components.JBScrollPane

import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.FlowLayout
import java.awt.event.MouseAdapter
import javax.swing.*


class UntrackedFilesDialog(
    project: Project,
    private val untrackedFiles: List<FilePath>
) : DialogWrapper(project) {

    private val selectedFiles = mutableSetOf<FilePath>()

    init {
        title = "Untracked Files Detected"
        init()
    }

    override fun createCenterPanel(): JComponent {
        val mainPanel = JBPanel<JBPanel<*>>(BorderLayout())
        mainPanel.add(JLabel("Select untracked files to add to commit:"), BorderLayout.NORTH)

        // Files panel with padding and scroll
        val filesPanel = JPanel()
        filesPanel.layout = BoxLayout(filesPanel, BoxLayout.Y_AXIS)
        filesPanel.border = BorderFactory.createEmptyBorder(5, 2, 10, 2)

        untrackedFiles.forEach { filePath ->
            val checkBox = JBCheckBox(filePath.presentableUrl).apply {
                border = BorderFactory.createEmptyBorder(2, 2, 2, 2)
                addActionListener {
                    if (isSelected) selectedFiles.add(filePath) else selectedFiles.remove(filePath)
                }
            }
            filesPanel.add(checkBox)
        }

        // Scroll pane for file list
        val scrollPane = JBScrollPane(filesPanel).apply {
            verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS
            horizontalScrollBarPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS
            preferredSize = Dimension(400, 300)
        }

        mainPanel.add(scrollPane, BorderLayout.CENTER)

        val buttonPanel = JPanel(FlowLayout(FlowLayout.RIGHT))

        val commitButton = JButton("Add to Commit").apply {
            addActionListener {
                close(OK_EXIT_CODE)
            }
        }

        val ignoreButton = JButton("Ignore").apply {
            addActionListener {
                selectedFiles.clear()
                close(OK_EXIT_CODE)
            }
        }

        val cancelButton = JButton("Cancel").apply {
            addActionListener {
                close(CANCEL_EXIT_CODE)
            }
        }

        buttonPanel.add(commitButton)
        buttonPanel.add(ignoreButton)
        buttonPanel.add(cancelButton)

        mainPanel.add(buttonPanel, BorderLayout.SOUTH)
        return mainPanel
    }

    override fun createActions(): Array<Action> {
        // Disable default actions since we're handling buttons directly in the panel
        return emptyArray()
    }

    fun getSelectedFiles(): Set<FilePath> = selectedFiles
}