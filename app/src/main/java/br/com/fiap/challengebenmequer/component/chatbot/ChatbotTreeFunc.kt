package br.com.fiap.challengebenmequer.component.chatbot

import android.content.Context
import br.com.fiap.challengebenmequer.R

// ─────────────────────────────────────────────────────────────────────────────
// Data model
data class ChatNode(
    val text: String,
    val options: MutableList<ChatOption> = mutableListOf()
)

data class ChatOption(
    val label: String,
    val next: ChatNode
)

sealed class ChatMessage(val text: String) {
    class Sent(text: String) : ChatMessage(text)
    class Received(text: String) : ChatMessage(text)
}

// ─────────────────────────────────────────────────────────────────────────────
// Build the conversation tree in one place
fun buildChatTree(context: Context): ChatNode {
    val res = context.resources

    val root = ChatNode(
        text = res.getString(R.string.chatbot_func_tree_root_text_1) + "\n" +
                res.getString(R.string.chatbot_func_tree_root_text_2)
    )

    val nodeRespiracao = ChatNode(
        text = res.getString(R.string.chatbot_func_tree_respiration_text_1) + "\n" +
                res.getString(R.string.chatbot_func_tree_respiration_text_2)
    )

    val nodeCausaAnsia = ChatNode(
        text = res.getString(R.string.chatbot_func_tree_causes_text_1) + "\n" +
                res.getString(R.string.chatbot_func_tree_causes_text_2)
    )

    val nodeJogoResp = ChatNode(
        text = res.getString(R.string.chatbot_func_tree_breath_game_text_1) + "\n" +
                res.getString(R.string.chatbot_func_tree_breath_game_text_2)
    )

    val nodeFraseMot = ChatNode(
        text = res.getString(R.string.chatbot_func_tree_motivational_quote_1) + "\n" +
                res.getString(R.string.chatbot_func_tree_motivational_quote_2)
    )

    val restartOption = ChatOption(res.getString(R.string.chatbot_func_tree_option_retry), root)

    listOf(nodeRespiracao, nodeCausaAnsia, nodeJogoResp, nodeFraseMot).forEach { node ->
        node.options.add(restartOption)
    }

    root.options.addAll(listOf(
        ChatOption(
            label = res.getString(R.string.chatbot_func_tree_option_anxious),
            next = ChatNode(
                text = res.getString(R.string.chatbot_func_tree_anxious_response_1) + "\n" +
                        res.getString(R.string.chatbot_func_tree_anxious_response_2),
                options = mutableListOf(
                    ChatOption(res.getString(R.string.chatbot_func_tree_option_control_breathing), nodeRespiracao),
                    ChatOption(res.getString(R.string.chatbot_func_tree_option_understand_anxiety), nodeCausaAnsia),
                    ChatOption(
                        label = res.getString(R.string.chatbot_func_tree_option_distract_me),
                        next = ChatNode(
                            text = res.getString(R.string.chatbot_func_tree_distract_text_1) + "\n" +
                                    res.getString(R.string.chatbot_func_tree_distract_text_2),
                            options = mutableListOf(
                                ChatOption(res.getString(R.string.chatbot_func_tree_option_breath_exercise), nodeJogoResp),
                                ChatOption(res.getString(R.string.chatbot_func_tree_option_motivational_phrase), nodeFraseMot),
                                restartOption
                            )
                        )
                    )
                )
            )
        ),
        ChatOption(
            label = res.getString(R.string.chatbot_func_tree_option_unmotivated),
            next = ChatNode(
                text = res.getString(R.string.chatbot_func_tree_unmotivated_text),
                options = mutableListOf(
                    ChatOption(res.getString(R.string.chatbot_func_tree_stuck_routine), ChatNode(
                        text = res.getString(R.string.chatbot_func_tree_stuck_response),
                        options = mutableListOf(restartOption)
                    )
                    ),
                    ChatOption(res.getString(R.string.chatbot_func_tree_no_purpose), ChatNode(
                        text = res.getString(R.string.chatbot_func_tree_no_purpose_response),
                        options = mutableListOf(restartOption)
                    )
                    ),
                    ChatOption(res.getString(R.string.chatbot_func_tree_mentally_tired), ChatNode(
                        text = res.getString(R.string.chatbot_func_tree_mentally_tired_response),
                        options = mutableListOf(restartOption)
                    )
                    )
                )
            )
        ),
        ChatOption(
            label = res.getString(R.string.chatbot_func_tree_overwhelmed),
            next = ChatNode(
                text = res.getString(R.string.chatbot_func_tree_overwhelmed_text),
                options = mutableListOf(
                    ChatOption(res.getString(R.string.chatbot_func_tree_how_to_know), ChatNode(
                        text = res.getString(R.string.chatbot_func_tree_how_to_know_response),
                        options = mutableListOf(restartOption)
                    )
                    ),
                    ChatOption(res.getString(R.string.chatbot_func_tree_relieve_stress), ChatNode(
                        text = res.getString(R.string.chatbot_func_tree_relieve_stress_response),
                        options = mutableListOf(restartOption)
                    )
                    ),
                    ChatOption(res.getString(R.string.chatbot_func_tree_talk_to), ChatNode(
                        text = res.getString(R.string.chatbot_func_tree_talk_to_response),
                        options = mutableListOf(restartOption)
                    )
                    )
                )
            )
        ),
        ChatOption(
            label = res.getString(R.string.chatbot_func_tree_want_to_feel_better),
            next = ChatNode(
                text = res.getString(R.string.chatbot_func_tree_feel_better_text),
                options = mutableListOf(
                    ChatOption(res.getString(R.string.chatbot_func_tree_feeling_sad), ChatNode(
                        text = res.getString(R.string.chatbot_func_tree_feeling_sad_text_1) + "\n" +
                                res.getString(R.string.chatbot_func_tree_feeling_sad_text_2),
                        options = mutableListOf(
                            ChatOption(res.getString(R.string.chatbot_func_tree_option_breath_exercise), nodeJogoResp),
                            ChatOption(res.getString(R.string.chatbot_func_tree_option_motivational_phrase), nodeFraseMot),
                            restartOption
                        )
                    )
                    ),
                    ChatOption(res.getString(R.string.chatbot_func_tree_feeling_anxious), ChatNode(
                        text = res.getString(R.string.chatbot_func_tree_feeling_anxious_response),
                        options = mutableListOf(restartOption)
                    )
                    ),
                    ChatOption(res.getString(R.string.chatbot_func_tree_feeling_tired), ChatNode(
                        text = res.getString(R.string.chatbot_func_tree_feeling_tired_response),
                        options = mutableListOf(restartOption)
                    )
                    ),
                    ChatOption(res.getString(R.string.chatbot_func_tree_feeling_unsure), ChatNode(
                        text = res.getString(R.string.chatbot_func_tree_feeling_unsure_response),
                        options = mutableListOf(restartOption)
                    )
                    )
                )
            )
        )
    ))

    return root
}
