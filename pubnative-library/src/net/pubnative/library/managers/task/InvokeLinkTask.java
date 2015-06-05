/**
 * Copyright 2014 PubNative GmbH
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package net.pubnative.library.managers.task;

import org.droidparts.concurrent.task.SimpleAsyncTask;
import org.droidparts.net.http.HTTPResponse;
import org.droidparts.net.http.RESTClient2;

import android.content.Context;

public class InvokeLinkTask extends SimpleAsyncTask<HTTPResponse>
{
    public interface InvokeLinkTaskListener
    {
        void onInvokeLinkTaskFinished(HTTPResponse response, InvokeLinkTask task);

        void onInvokeLinkTaskFailed(Exception exception, InvokeLinkTask task);
    }

    private final RESTClient2      restClient;
    public final String            link;
    private InvokeLinkTaskListener listener;

    public InvokeLinkTask(Context ctx, InvokeLinkTaskListener listener, String link)
    {
        super(ctx, null);
        restClient = new RESTClient2(ctx);
        this.link = link;
        this.listener = listener;
    }

    public Context getContext()
    {
        return super.getContext();
    }

    @Override
    protected HTTPResponse onExecute() throws Exception
    {
        return restClient.get(link);
    }

    @Override
    public void onPostExecuteSuccess(HTTPResponse response)
    {
        super.onPostExecuteSuccess(response);
        if (this.listener != null)
        {
            this.listener.onInvokeLinkTaskFinished(response, this);
        }
    }

    @Override
    public void onPostExecuteFailure(Exception exception)
    {
        super.onPostExecuteFailure(exception);
        if (this.listener != null)
        {
            this.listener.onInvokeLinkTaskFailed(exception, this);
        }
    }
}
