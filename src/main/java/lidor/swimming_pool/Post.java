package lidor.swimming_pool;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Post
{
    public String key;
    public String uid;
    public String title;
    public String body;
    public int likes = 0;

    public Post()
    {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Post(String uid, String title, String body, int likes,String key)
    {
        this.uid = uid;
        this.likes = likes;
        this.title = title;
        this.body = body;
        this.key = key;
    }


}

