/******************************************************************************
 *
 *  Copyright 2014 Paphus Solutions Inc.
 *
 *  Licensed under the Eclipse Public License, Version 1.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.eclipse.org/legal/epl-v10.html
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 ******************************************************************************/

package org.botlibre.sdk.activity.actions;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import org.botlibre.sdk.R;
import org.botlibre.sdk.activity.MainActivity;
import org.botlibre.sdk.config.ContentConfig;

public class HttpGetTagsAction extends HttpAction {
	ContentConfig config;
	Object[] tags;

	public HttpGetTagsAction(Activity activity, String type) {
		super(activity);
		this.config = new ContentConfig();
		this.config.type = type;
	}

	@Override
	protected String doInBackground(Void... params) {
		if (this.config.type.equals("Bot") && MainActivity.tags != null) {
			this.tags = MainActivity.tags;
		} else if (this.config.type.equals("Forum") && MainActivity.forumTags != null) {
			this.tags = MainActivity.forumTags;
		} else if (this.config.type.equals("Post") && MainActivity.forumPostTags != null) {
			this.tags = MainActivity.forumPostTags;
		} else if (this.config.type.equals("Channel") && MainActivity.channelTags != null) {
			this.tags = MainActivity.channelTags;
		} else if (this.config.type.equals("Avatar") && MainActivity.avatarTags != null) {
			this.tags = MainActivity.avatarTags;
		} else if (this.config.type.equals("Script") && MainActivity.scriptTags != null) {
			this.tags = MainActivity.scriptTags;
		} else if (this.config.type.equals("Domain")) {
			this.tags = new Object[0];
		} else {
			try {
				this.tags = MainActivity.connection.getTags(this.config).toArray();
			} catch (Exception exception) {
				this.exception = exception;
			}
		}
		return "";
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void onPostExecute(String xml) {
		if (this.exception != null) {
			return;
		}
		if (this.config.type.equals("Bot")) {
			MainActivity.tags = this.tags;
		} else if (this.config.type.equals("Forum")) {
			MainActivity.forumTags = this.tags;
		} else if (this.config.type.equals("Post")) {
			MainActivity.forumPostTags = this.tags;
		} else if (this.config.type.equals("Channel")) {
			MainActivity.channelTags = this.tags;
		} else if (this.config.type.equals("Avatar")) {
			MainActivity.avatarTags = this.tags;
		} else if (this.config.type.equals("Script")) {
			MainActivity.scriptTags = this.tags;
		}
		
        final AutoCompleteTextView tagsText = (AutoCompleteTextView)this.activity.findViewById(R.id.tagsText);
        if (tagsText != null) {
	        ArrayAdapter adapter = new ArrayAdapter(this.activity,
	                android.R.layout.select_dialog_item, this.tags);
	        tagsText.setThreshold(0);
	        tagsText.setAdapter(adapter);
	        tagsText.setOnTouchListener(new View.OnTouchListener() {
		    	   @Override
		    	   public boolean onTouch(View v, MotionEvent event){
		    		   tagsText.showDropDown();
		    		   return false;
		    	   }
		    	});
        }
	}
}