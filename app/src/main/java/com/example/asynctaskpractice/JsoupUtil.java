package com.example.asynctaskpractice;



import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



/**
 *
 * @author wwj_748
 * @date 2014/8/10
 */
public class JsoupUtil {
	public static boolean contentFirstPage = true; // 第一页
	public static boolean contentLastPage = true; // 最后一页
	public static boolean multiPages = false; // 多页
	private static final String BLOG_URL = "http://blog.csdn.net"; // CSDN博客地址

	// 链接样式文件，代码块高亮的处理
	public final static String linkCss = "<script type=\"text/javascript\" src=\"file:///android_asset/shCore.js\"></script>"
			+ "<script type=\"text/javascript\" src=\"file:///android_asset/shBrushJScript.js\"></script>"
			+ "<script type=\"text/javascript\" src=\"file:///android_asset/shBrushJava.js\"></script>"
			+ "<link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/shThemeDefault.css\">"
			+ "<link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/shCore.css\">"
			+ "<script type=\"text/javascript\">SyntaxHighlighter.all();</script>";

	public static void resetPages() {
		contentFirstPage = true;
		contentLastPage = true;
		multiPages = false;
	}

	/**
	 * 使用Jsoup解析html文档
	 *
	 * @param blogType
	 * @param doc
	 * @return
	 */
	public static List<NewsItem> getNewsItemList(int blogType, Document doc) {
		// Log.e("URL---->", str);
//		List<BlogItem> list = new ArrayList<BlogItem>();
		// 升华新闻对象列表
		List<NewsItem> newsList = new ArrayList<NewsItem>();
		// 获取文档对象
//		Document doc = Jsoup.parse(str);
		// Log.e("doc--->", doc.toString());
		// 获取class="article_item"的所有元素
//		String newslistHtml = doc.getElementsByClass("border");

//		System.out.print(element);

//		Elements blogList = doc.getElementsByClass("article_item");

		// Log.e("elements--->", blogList.toString());

//		Document newsDoc = Jsoup.parse(newslistHtml);

		Elements tags_a = doc.getElementsByClass("border").get(0).select("a");

//		Elements tags_a = newsDoc.select("a");
		Elements tags_time = doc.getElementsByClass("border").get(0).select("font");

//		tags_a.size();

		if(tags_a.size()!= 0&& tags_time.size()!= 0){
			//解析到网页准备解析内容，获取新闻实体类

			for (int i = 0; i<tags_a.size(); i++){
				NewsItem newsItem = new NewsItem();
				String title = tags_a.get(i).text();
				newsItem.setTitle(title);
				String date = tags_time.get(i).text();
				newsItem.setDate(date);
				String link = tags_a.get(i).attr("href");
				newsItem.setLink(link);


				//TODO
				//调用Get方法获取Item的连接，并解析获取图片地址和简要的信息描述
//				String[] content_imgUrl = getContent_Pic(HttpUtil.httpGetDoc(URLUtil.BASE + link));
//				newsItem.setContent(content_imgUrl[0]);
//				newsItem.setImgLink(URLUtil.BASE + content_imgUrl[1]);

				String authorInTitle = tags_a.get(i).attr("title");
				//使用正则表达式匹配出作者
				String author = matcherauthor(authorInTitle);
				newsItem.setAuthor(author);
				//获取阅读次数
				String readTimes = matcherReadTimes(authorInTitle);
				newsItem.setReadTimes(readTimes);

				newsItem.setMsg("1");

				newsItem.setType(1);

//			// 没有图片
//				newsItem.setImgLink(null);

				newsList.add(newsItem);
			}

		}

		return newsList;


//		for (Element blogItem : blogList) {
//			BlogItem item = new BlogItem();
//			String title = blogItem.select("h1").text(); // 得到标题
//			// System.out.println("title----->" + title);
//			String description = blogItem.select("div.article_description")
//					.text();
//			// System.out.println("descrition--->" + description);
//			String msg = blogItem.select("div.article_manage").text();
//			// System.out.println("msg--->" + msg);
//			String date = blogItem.getElementsByClass("article_manage").get(0)
//					.text();
//			// System.out.println("date--->" + date);
//			String link = BLOG_URL
//					+ blogItem.select("h1").select("a").attr("href");
//			// System.out.println("link--->" + link);
//			item.setTitle(title);
//			item.setMsg(msg);
//			item.setContent(description);
//			item.setDate(date);
//			item.setLink(link);
//			item.setType(blogType);
//
//			// 没有图片
//			item.setImgLink(null);
//			list.add(item);
//
//		}
//		return list;
	}

	public static String[] getContent_Pic(Document doc) {
		//TODO

//		Elements tags_a = doc.getElementsByClass("border").get(0).select("a");

		Elements content_p_tags = doc.getElementsByAttributeValueStarting("height", "200").select("p");

		String content = null;
		String img_url = null;

		for (int i = 0; i< content_p_tags.size(); i++){

			int textLength = content_p_tags.get(i).text().length();
			Elements hasImg = content_p_tags.get(i).select("img");
			if(hasImg.size() == 0 && textLength > 10){
				if(textLength > 8 && textLength < 30) {
					content = content_p_tags.get(i).text().substring(0, textLength);
				}else{
					content = content_p_tags.get(i).text().substring(0, textLength);
				}
			}

			if(hasImg.size() != 0 && img_url == null){
				img_url = content_p_tags.get(i).select("img").attr("src");
			}
		}

		String[] content_imgUrl = new String[2];
		if(content != null || img_url != null){
			content_imgUrl[0] = content;
			content_imgUrl[1] = img_url;
		}
		return content_imgUrl;

	}

	private static String matcherReadTimes(String authorInTitle) {
		String readTimes = null;
		Pattern p = Pattern.compile("(点击次数：[\\d]+)");
		Matcher matcher = p.matcher(authorInTitle);
		if (matcher.find()) {
			int start = matcher.start();
			int end = matcher.end();
			return authorInTitle.substring(start+5, end);
		}else{
			return "2";
		}
	}


	private static String matcherauthor(String authorInTitle) {
		String author = null;
		Pattern p = Pattern.compile("(作    者：[\\u4e00-\\u9fa5]+)");
		Matcher matcher = p.matcher(authorInTitle);
		if (matcher.find()) {
			int start = matcher.start();
			int end = matcher.end();
			return authorInTitle.substring(start+7, end);
		}else{
			return "未署名";
		}
	}

	/**
	 * 使用Jsoup解析html文档
	 *
	 * @param blogType
	 * @param str
	 * @return
	 */


	/**
	 * 扒取传入url地址的博客详细内容
	 *
	 * @param url
	 * @param str
	 * @return
	 */


	/**
	 * 获取博客评论列表
	 *
	 * @unused 无用
	 * @param str
	 * @return
	 */
	// public static List<Comment> getComment(String str) {
	// List<Comment> list = new ArrayList<Comment>();
	//
	// // 获取文档对象
	// Document doc = Jsoup.parse(str);
	// // 获得id为comment_list的元素
	// Element commentClass = doc.getElementsByClass("comment_class").get(0);
	//
	// Element commentList = commentClass.getElementById("comment_list");
	//
	// // 获得所有评论主题
	// Elements commentTopics = commentList
	// .select("div.comment_item.comment_topic");
	// // 遍历所有评论主题
	// for (Element commentTopic : commentTopics) {
	// Comment topic = new Comment();
	// topic.setName(commentTopic.select("a.username").text());
	// topic.setDate(commentTopic.select("span.ptime").text());
	// topic.setContent(commentTopic.select("dd.comment_body").text());
	// topic.setPic(commentTopic.select("dd.comment_userface")
	// .select("img").attr("src"));
	//
	// // 获取评论回复
	// Elements commentReplies = commentTopic
	// .getElementsByClass("comment_reply");
	// if (commentReplies.size() == 0) {
	// topic.setReplyCount("");
	// } else {
	// topic.setReplyCount("回复: " + commentReplies.size());
	// }
	// topic.setType(Constants.DEF_COMMENT_TYPE.PARENT);
	// list.add(topic);
	//
	// if (commentReplies.size() != 0) {
	// for (Element replyElement : commentReplies) {
	// Comment reply = new Comment();
	// reply.setName(replyElement.select("a.username").text());
	// reply.setDate(replyElement.select("span.ptime").text());
	// reply.setContent(replyElement.select("dd.comment_body")
	// .text());
	// reply.setPic(replyElement.select("dd.comment_userface")
	// .select("img").attr("src"));
	// Elements commentReplies2 = replyElement
	// .getElementsByClass("comment_reply");
	// reply.setReplyCount("" + commentReplies2.size());
	// reply.setType(Constants.DEF_COMMENT_TYPE.CHILD);
	//
	// list.add(reply);
	// }
	// }
	// }
	// return list;
	//
	// }

	/**
	 * 获取博文评论列表
	 *
	 * @param str
	 *            json字符串
	 * @return
	 */

	/**
	 * 获得博主个人资料
	 *
	 * @param str
	 * @return
	 */


	/**
	 * 半角转换为全角 全角---指一个字符占用两个标准字符位置。 半角---指一字符占用一个标准的字符位置。
	 *
	 * @param input
	 * @return
	 */
	public static String ToDBC(String input) {
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 12288) {
				c[i] = (char) 32;
				continue;
			}
			if (c[i] > 65280 && c[i] < 65375)
				c[i] = (char) (c[i] - 65248);
		}
		return new String(c);
	}

}
