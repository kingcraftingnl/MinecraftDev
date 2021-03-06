/*
 * Minecraft Dev for IntelliJ
 *
 * https://minecraftdev.org
 *
 * Copyright (c) 2017 minecraft-dev
 *
 * MIT License
 */

package com.demonwav.mcdev.platform

import com.demonwav.mcdev.MinecraftSettings
import com.demonwav.mcdev.facet.MinecraftFacet
import com.intellij.ide.projectView.PresentationData
import com.intellij.ide.projectView.ProjectViewNode
import com.intellij.ide.projectView.ProjectViewNodeDecorator
import com.intellij.ide.projectView.impl.nodes.PsiDirectoryNode
import com.intellij.openapi.module.ModuleManager
import com.intellij.openapi.module.ModuleUtilCore
import com.intellij.openapi.roots.ModuleRootManager
import com.intellij.packageDependencies.ui.PackageDependenciesNode
import com.intellij.ui.ColoredTreeCellRenderer

/**
 * This class sets the icons for the modules in the project view.
 */
class MinecraftProjectViewNodeDecorator : ProjectViewNodeDecorator {

    override fun decorate(node: ProjectViewNode<*>, data: PresentationData) {
        if (!MinecraftSettings.instance.isShowProjectPlatformIcons) {
            return
        }

        val project = node.project ?: return

        if (node !is PsiDirectoryNode) {
            return
        }

        val directory = node.value
        val module = ModuleUtilCore.findModuleForPsiElement(directory) ?: return

        val rootManager = ModuleRootManager.getInstance(module)
        // Make sure there is at least a root to go off of
        if (rootManager.contentRoots.isEmpty()) {
            return
        }

        // Get the root and compare it to the node
        val root = rootManager.contentRoots[0]
        if (root != node.getVirtualFile()) {
            return
        }

        val children = MinecraftFacet.getChildInstances(module)
        if (children.isEmpty()) {
            return
        }

        val manager = ModuleManager.getInstance(project)
        val path = manager.getModuleGroupPath(module)
        if (path == null) {
            data.setIcon(children.iterator().next().getIcon())
            return
        }

        val testModule = manager.findModuleByName(path.last())
        if (module !== testModule) {
            return
        }

        data.setIcon(children.iterator().next().getIcon())
    }

    override fun decorate(node: PackageDependenciesNode, cellRenderer: ColoredTreeCellRenderer) {}
}
