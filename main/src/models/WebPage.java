package models;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.Query;
import org.hibernate.Session;

import com.mkyong.persistence.HibernateUtil;

@Entity
@Table(name = "webpage", catalog = "mir_p_ph3", uniqueConstraints = { @UniqueConstraint(columnNames = "URL") })
public class WebPage implements java.io.Serializable, Comparable<WebPage> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -961677318625003452L;
	private Integer webPageId;
	private String url;
	private Set<WebPage> linkedWebPages = new HashSet<WebPage>(5);

	public WebPage() {
	}

	public static void testAssossiates() {
		WebPage page = new WebPage();
		page
				.setURL("http://www.mkyong.com/java/how-to-write-to-file-in-java-bufferedwriter-example/");
		page.save();
		WebPage page2 = new WebPage();
		page2
				.setURL("http://www222222.mkyong.com/java/how-to-write-to-file-in-java-bufferedwriter-example/");
		page2.save();
		page.getLinkedWebPages().add(page2);
		page.getLinkedWebPages().add(page);
		page2.getLinkedWebPages().add(page);

		page.save();
		page2.save();
	}

	public static void testFilter() {
		WebPage w = getByURL("http://www.mkyong.com/java/how-to-write-to-file-in-java-bufferedwriter-example/");
		System.out.println(w.getWebPageId());
	}

	public static void main(String[] args) {
		// /test
		//testFilter();
		testAssossiates();
	}

	public WebPage(String url) {
		this.url = url;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "WEBPAGE_ID", unique = true, nullable = false)
	public Integer getWebPageId() {
		return this.webPageId;
	}

	public void setWebPageId(Integer webPageId) {
		this.webPageId = webPageId;
	}

	@Column(name = "URL", unique = true, nullable = false, length = 255)
	public String getURL() {
		return this.url;
	}

	public void setURL(String url) {
		this.url = url;
	}

	public void save() {
		Session session = HibernateUtil.getSession();

		session.beginTransaction();

		session.save(this);
		// session.delete(stock);

		session.getTransaction().commit();

	}

	public void delete() {
		Session session = HibernateUtil.getSession();

		session.beginTransaction();

		session.delete(this);

		session.getTransaction().commit();

	}

	public static WebPage getByURL(String targeturl) {
		Session session = HibernateUtil.getSession();
		session.beginTransaction();
		// String hql = String.format("delete from %s",myTable);
		WebPage wp = null;
		if (true) {
			 List<WebPage> lw = (List<WebPage>) session.createQuery(
					"from WebPage where URL = :targeturl").setString(
					"targeturl", targeturl).list();
			 if(lw.size()==0){
				 return null;
			 }else{
				 return lw.get(0);
			 }
		} else {
			wp = (WebPage) session.createQuery(
					"from WebPage where URL = '"+targeturl+"'").list().get(0);
		}

		// query = session.createQuery("delete from LinkedWebPages");
		// query.executeUpdate();

		session.getTransaction().commit();
		return wp;
	}

	public static void trunc() {
		Session session = HibernateUtil.getSession();

		List<WebPage> webpages = (List<WebPage>) session.createCriteria(
				WebPage.class).list();

		for (WebPage webPage : webpages) {
			webPage.getLinkedWebPages().clear();
			webPage.save();
		}

		session.beginTransaction();
		// String hql = String.format("delete from %s",myTable);
		Query query = session.createQuery("delete from WebPage");
		query.executeUpdate();

		// query = session.createQuery("delete from LinkedWebPages");
		// query.executeUpdate();

		session.getTransaction().commit();

	}

	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	@JoinTable(name = "linkedwebpages", catalog = "mir_p_ph3")
	public Set<WebPage> getLinkedWebPages() {
		return this.linkedWebPages;
	}

	public void setLinkedWebPages(Set<WebPage> linkedWebPages) {
		this.linkedWebPages = linkedWebPages;
	}
	
	@Override
	public boolean equals(Object obj) {
		return ((WebPage)obj).getURL().equals(this.getURL());
	}
	
	public static List<WebPage> getAll(){
		return HibernateUtil.getSession().createCriteria(WebPage.class).list();
	}

	@Override
	public String toString() {
		return "id: "+this.getWebPageId()+"-url: "+this.getURL()+"-END";
	}
	public int compareTo(WebPage o) {
		return this.getURL().compareTo(((WebPage)o).getURL());
	}

	
	
}
