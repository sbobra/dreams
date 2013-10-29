package com.example.dreams.controller;

import java.util.Arrays;
import java.util.List;

import android.util.Log;

import com.example.dreams.model.Sleep;
import com.example.dreams.model.State;
import com.example.dreams.view.JournalFragment;
import com.stackmob.sdk.api.StackMobOptions;
import com.stackmob.sdk.api.StackMobQuery;
import com.stackmob.sdk.callback.StackMobQueryCallback;
import com.stackmob.sdk.exception.StackMobException;

public class JournalFragmentController {
	private JournalFragment fragment;
	Sleep[] sleepList;

	public JournalFragmentController(JournalFragment journalFragment) {
		this.fragment = journalFragment;

	}

	public void updateTable() {
		for (int i = 0; i < sleepList.length; i++) {
			final Sleep s = sleepList[i];
			fragment.getActivity().runOnUiThread(new Runnable() {
				public void run() {
					fragment.populate(s);
				}
			});

		}
	}

	public void onRefresh() {
		fragment.clearTable();
		// request dream data

		Sleep.query(
				Sleep.class,
				new StackMobQuery().fieldIsEqualTo("sm_owner",
						("user/" + State.getInstance().getUsername()))
						.fieldIsNotNull("dream"), StackMobOptions.https(true)
						.withDepthOf(3), new StackMobQueryCallback<Sleep>() {
					@Override
					public void failure(StackMobException e) {
						Log.i("JournalFragmentController", e.getMessage());
					}

					@Override
					public void success(List<Sleep> arg0) {
						Log.i("JournalFragmentController", arg0.size()
								+ " dreams received!");
						sleepList = new Sleep[arg0.size()];
						for (int i = 0; i<sleepList.length;i++) {
							sleepList[i] = arg0.get(i);
						}
						Arrays.sort(sleepList);
						updateTable();

					}
				});
		// String query = "user/" + State.getInstance().getUsername();
		// Log.i("JournalFragmentController", query);
		// StackMobQuery q = new StackMobQuery(query);
		// User.query(
		// User.class,
		// q,
		// StackMobOptions.https(true)
		// .withSelectedFields(Arrays.asList("dream_list"))
		// .withDepthOf(1), new StackMobQueryCallback<User>() {
		// @Override
		// public void failure(StackMobException e) {
		// Log.i("JournalFragmentController", e.getMessage());
		// }
		//
		// @Override
		// public void success(List<User> arg0) {
		// Log.i("JournalFragmentController", arg0.get(0)
		// .getName() + " = name");
		// Log.i("JournalFragmentController", arg0.get(0)
		// .getDreamList().size()
		// + " dreams received!");
		// for (int i = 0; i < arg0.get(0).getDreamList().size(); i++) {
		// Sleep s = arg0.get(0).getDreamList().get(i);
		// fragment.populate(s);
		// }
		//
		// }
		// });
	}
}
