package com.newsreader.guardian.guardiantech;

/**
 * Created by Sam on 20/12/2016.
 * Each list item on the main screen displays relevant text and information about the story.

 Required fields include the title of the article and the name of the section that it belongs to.

 Optional fields (if available) : author name , date published

 fields for information about the story
 "type": "interactive",
 "sectionName": "Life and style",
 */

public class NewsStory {
    private String Title;
    private String Date;
    private String ArticleType;
    private String SectionName;
    private String URL;

    /**
     * Create a new NewsStory object
     * @param title news story headline
     * @param sectionName part of newspaper the story is from
     * @param articleType article / interactive etc.
     * @param date the story was printed
     */
    public NewsStory(String title, String sectionName, String articleType, String date, String url) {
        Title = title;
        SectionName = sectionName;
        ArticleType = articleType;
        Date = date;
        URL = url;
    }
    public String getTitle() { return Title; }
    public String getSectionName() { return SectionName; }
    public String getArticleType() { return ArticleType; }
    public String getDate() { return Date; }
    public String getUrl() { return URL; }

}
