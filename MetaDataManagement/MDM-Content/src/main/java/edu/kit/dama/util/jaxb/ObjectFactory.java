//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.04.19 at 11:04:28 AM CEST 
//


package edu.kit.dama.util.jaxb;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the net.kitdatamanager.dama.basemetadata package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: net.kitdatamanager.dama.basemetadata
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link BaseMetadata }
     * 
     */
    public BaseMetadata createBaseMetadata() {
        return new BaseMetadata();
    }

    /**
     * Create an instance of {@link Investigation }
     * 
     */
    public Investigation createInvestigation() {
        return new Investigation();
    }

    /**
     * Create an instance of {@link Investigation.Participants }
     * 
     */
    public Investigation.Participants createInvestigationParticipants() {
        return new Investigation.Participants();
    }

    /**
     * Create an instance of {@link Investigation.MetadataSchemas }
     * 
     */
    public Investigation.MetadataSchemas createInvestigationMetadataSchemas() {
        return new Investigation.MetadataSchemas();
    }

    /**
     * Create an instance of {@link Study }
     * 
     */
    public Study createStudy() {
        return new Study();
    }

    /**
     * Create an instance of {@link Study.Relations }
     * 
     */
    public Study.Relations createStudyRelations() {
        return new Study.Relations();
    }

    /**
     * Create an instance of {@link BaseMetadata.DigitalObject }
     * 
     */
    public BaseMetadata.DigitalObject createBaseMetadataDigitalObject() {
        return new BaseMetadata.DigitalObject();
    }

    /**
     * Create an instance of {@link OrganizationUnit }
     * 
     */
    public OrganizationUnit createOrganizationUnit() {
        return new OrganizationUnit();
    }

    /**
     * Create an instance of {@link User }
     * 
     */
    public User createUser() {
        return new User();
    }

    /**
     * Create an instance of {@link Investigation.Participants.Participant }
     * 
     */
    public Investigation.Participants.Participant createInvestigationParticipantsParticipant() {
        return new Investigation.Participants.Participant();
    }

    /**
     * Create an instance of {@link Investigation.MetadataSchemas.MetadataSchema }
     * 
     */
    public Investigation.MetadataSchemas.MetadataSchema createInvestigationMetadataSchemasMetadataSchema() {
        return new Investigation.MetadataSchemas.MetadataSchema();
    }

    /**
     * Create an instance of {@link Study.Relations.Relation }
     * 
     */
    public Study.Relations.Relation createStudyRelationsRelation() {
        return new Study.Relations.Relation();
    }

    /**
     * Create an instance of {@link BaseMetadata.DigitalObject.Experimenters }
     * 
     */
    public BaseMetadata.DigitalObject.Experimenters createBaseMetadataDigitalObjectExperimenters() {
        return new BaseMetadata.DigitalObject.Experimenters();
    }

}
