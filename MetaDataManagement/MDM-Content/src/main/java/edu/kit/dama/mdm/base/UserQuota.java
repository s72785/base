package edu.kit.dama.mdm.base;

import edu.kit.dama.authorization.entities.UserId;
import edu.kit.dama.mdm.base.interfaces.IDefaultUserData;
import edu.kit.dama.mdm.core.tools.DateTester;
import edu.kit.dama.util.Constants;
import org.eclipse.persistence.oxm.annotations.XmlNamedAttributeNode;
import org.eclipse.persistence.oxm.annotations.XmlNamedObjectGraph;
import org.eclipse.persistence.oxm.annotations.XmlNamedObjectGraphs;
import org.eclipse.persistence.queries.FetchGroupTracker;
import org.eclipse.persistence.sessions.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by vv01f on 02.05.17.
 */
//@Table(name = "userquota")
@Entity
@XmlNamedObjectGraphs({
        @XmlNamedObjectGraph(
                name = "simple",
                attributeNodes = {
                        @XmlNamedAttributeNode("id"),
                        @XmlNamedAttributeNode("uid")
                }),
        @XmlNamedObjectGraph(
                name = "default",
                attributeNodes = {
                        @XmlNamedAttributeNode("id"),
                        @XmlNamedAttributeNode("uid"),
                        @XmlNamedAttributeNode("quota")
                })})
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
@NamedEntityGraphs({
        @NamedEntityGraph(
                name = "UserQuota.simple",
                includeAllAttributes = false,
                attributeNodes = {
                        @NamedAttributeNode("id")}),
        @NamedEntityGraph(
                name = "UserQuota.default",
                includeAllAttributes = false,
                attributeNodes = {
                        @NamedAttributeNode("id"),
                        @NamedAttributeNode("uid"),
                        @NamedAttributeNode("quota")
                })
})
public class UserQuota implements Serializable, FetchGroupTracker
{

//    private final static Logger LOGGER = LoggerFactory.getLogger(UserQuota.class);

    //Constructor disabled for usage
    public UserQuota(){ }

    private static final long serialVersionUID = 20170428L;
    public static final UserQuota WORLD_QUOTA = factoryWorldQuota();

    // members from table userquota
//    @Transient
//    @Column(name = "quota")
    private Long quota;//userquota for e.g. max. filesize

//    @Transient
//    @Column(name = "uid")
    private Long uid;//references id in UserData

    //    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;//serial

    public Long getUid() { return uid; }

    public void setUid(Long p) {
        this.uid = p;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long p) {
        this.id = p;
    }

    public Long getQuota() {
        return quota;
    }

    public void setQuota(Long p) {
        this.quota = p;
    }

    //Initialisation, a UserQuota object should not return `null` for any member
    private static UserQuota factoryWorldQuota() {
        UserQuota noQuota = new UserQuota();
        noQuota.setId(0l);
        noQuota.setUid(0l);
        noQuota.setQuota(0l);
        return noQuota ;
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder("");
        buffer  .append( "Quota\n" )
                .append( "-----\n" )
                .append( getId() )
                .append( ":\n  User " )
                .append( getUid() )
                .append( "\n  limited to " )
                .append( getQuota() )
                .append( " Byte" );
        return buffer.toString();
    }

    @Override
    public boolean equals(Object other) {
        boolean equals = true;
        if (this == other) {
            return equals;
        }
        if (other != null && (getClass() == other.getClass())) {
            UserQuota otherQuota = (UserQuota) other;
            if (id != null) {
                equals = equals && (id.equals(otherQuota.id));
            } else {
                equals = equals && (otherQuota.id == null);
            }
            if (equals && (uid != null)) {
                equals = equals && (uid.equals(otherQuota.uid));
            } else {
                equals = equals && (otherQuota.uid == null);
            }
            if (equals && (quota != null)) {
                equals = equals && (quota.equals(otherQuota.quota));
            } else {
                equals = equals && (otherQuota.quota == null);
            }
        } else {
            equals = false;
        }

        return equals;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 37 * hash + (this.uid != null ? this.uid.hashCode() : 0);
        hash = 37 * hash + (this.quota != null ? this.quota.hashCode() : 0);
        return hash;
    }
    private transient org.eclipse.persistence.queries.FetchGroup fg;
    private transient Session sn;

    @Override
    public org.eclipse.persistence.queries.FetchGroup _persistence_getFetchGroup() { return this.fg; }

    @Override
    public void _persistence_setFetchGroup(org.eclipse.persistence.queries.FetchGroup fg) { this.fg = fg; }

    @Override
    public boolean _persistence_isAttributeFetched(String string) { return true; }

    @Override
    public void _persistence_resetFetchGroup() { }

    @Override
    public boolean _persistence_shouldRefreshFetchGroup() { return false; }

    @Override
    public void _persistence_setShouldRefreshFetchGroup(boolean bln) { }

    @Override
    public Session _persistence_getSession() { return sn; }

    @Override
    public void _persistence_setSession(Session sn) { this.sn = sn; }

}
