package com.inscripts.cometchatpulse.demo.Presenters;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.core.ConversationsRequest;
import com.cometchat.pro.core.UsersRequest;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.helpers.CometChatHelper;
import com.cometchat.pro.models.Conversation;
import com.cometchat.pro.models.CustomMessage;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.MediaMessage;
import com.cometchat.pro.models.MessageReceipt;
import com.cometchat.pro.models.TextMessage;
import com.cometchat.pro.models.User;
import com.inscripts.cometchatpulse.demo.Base.Presenter;
import com.inscripts.cometchatpulse.demo.Contracts.ContactsContract;
import com.inscripts.cometchatpulse.demo.Contracts.RecentsContract;
import com.inscripts.cometchatpulse.demo.Utils.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import timber.log.Timber;


public class RecentsListPresenter extends Presenter<RecentsContract.RecentsView>
        implements RecentsContract.RecentsPresenter {

    private ConversationsRequest conversationRequest;

    private static final String TAG = "ContactsListPresenter";

    @Override
    public void fetchConversations(Context context) {

        conversationRequest = new ConversationsRequest.ConversationsRequestBuilder().setLimit(100).build();
        conversationRequest.fetchNext(new CometChat.CallbackListener<List<Conversation>>() {
            @Override
            public void onSuccess(List<Conversation> conversations) {
                Logger.error(TAG, " " + conversations.size());

                getBaseView().setRecentAdapter(conversations);
            }

            @Override
            public void onError(CometChatException e) {
                Timber.d("fetchNext onError: %s", e.getMessage());
            }
        });
    }

    @Override
    public void addMessageListener(String messageListener) {
        CometChat.addMessageListener(messageListener, new CometChat.MessageListener() {
            @Override
            public void onTextMessageReceived(TextMessage message) {
                   getBaseView().refreshConversation(message);

            }

            @Override
            public void onMediaMessageReceived(MediaMessage message) {
                getBaseView().refreshConversation(message);
            }

            @Override
            public void onCustomMessageReceived(CustomMessage message) {
                getBaseView().refreshConversation(message);
            }

        });
    }

    @Override
    public void removeMessageListener(String messageListener) {
        CometChat.removeMessageListener(messageListener);
    }


//    @Override
//    public void searchConversation(String s) {
//
//       ConversationsRequest conversationsRequest= new ConversationsRequest.ConversationsRequestBuilder().setLimit(100).build();
//       List<Conversation> hashMap = new ArrayList<>();
//       conversationsRequest.fetchNext(new CometChat.CallbackListener<List<Conversation>>() {
//           @Override
//           public void onSuccess(List<Conversation> conversations) {
//                for (Conversation conversation :conversations) {
//                    if (s!=null) {
//                        if (conversation.getConversationId().contains(s)) {
//                            hashMap.add(conversation);
//                        } else {
//                            if (conversation.getConversationType().equals(CometChatConstants.CONVERSATION_TYPE_USER) && ((User) conversation.getConversationWith()).getName().toLowerCase().contains(s)) {
//                                Log.e(TAG, "onSuccess: " + s + "=" + ((User) conversation.getConversationWith()).getName().toLowerCase());
//                                hashMap.add(conversation);
//                            } else if (conversation.getConversationType().equals(CometChatConstants.CONVERSATION_TYPE_GROUP) && ((Group) conversation.getConversationWith()).getName().toLowerCase().contains(s)) {
//                                Log.e(TAG, "Group: " + ((Group) conversation.getConversationWith()).getName().toLowerCase() + "=" + s);
//                                hashMap.add(conversation);
//                            }
//                        }
//                    }
//                    else
//                    {
//                        hashMap.add(conversation);
//                    }
//                }
//               getBaseView().setFilterList(hashMap);
//           }
//           @Override
//           public void onError(CometChatException e) {
//               Timber.d("onError: fetchNext %s", e.getMessage());
//           }
//       });
//    }

    @Override
    public void updateConversation() {

    }
}
