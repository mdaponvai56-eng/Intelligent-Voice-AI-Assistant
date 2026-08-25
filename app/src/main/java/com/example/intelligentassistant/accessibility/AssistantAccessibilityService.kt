package com.example.intelligentassistant.accessibility

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

class AssistantAccessibilityService : AccessibilityService() {

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // This service is now active and can read accessibility events
        // Log screen content when requested by the assistant
    }

    override fun onInterrupt() {
        // Accessibility service was interrupted
    }

    fun getScreenContent(): String {
        val rootNode = rootInActiveWindow ?: return "No active window"
        return extractText(rootNode)
    }

    private fun extractText(node: AccessibilityNodeInfo?): String {
        if (node == null) return ""
        val sb = StringBuilder()

        if (node.text != null && node.text.isNotEmpty()) {
            sb.append(node.text).append(" ")
        }

        for (i in 0 until (node.childCount ?: 0)) {
            val child = node.getChild(i)
            sb.append(extractText(child))
        }

        return sb.toString()
    }

    fun performClickAction(nodeText: String): Boolean {
        val rootNode = rootInActiveWindow ?: return false
        return findAndClickNode(rootNode, nodeText)
    }

    private fun findAndClickNode(node: AccessibilityNodeInfo?, text: String): Boolean {
        if (node == null) return false

        if (node.text == text && node.isClickable) {
            node.performAction(AccessibilityNodeInfo.ACTION_CLICK)
            return true
        }

        for (i in 0 until (node.childCount ?: 0)) {
            val child = node.getChild(i)
            if (findAndClickNode(child, text)) {
                return true
            }
        }

        return false
    }
}
