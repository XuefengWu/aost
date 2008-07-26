package aost.datadriven.object.mapping
/**
 *
 * The Registry to hold all FieldSet defined
 * 
 * @author: Jian Fang (John.Jian.Fang@gmail.com)
 *
 * Date: Jul 23, 2008
 *
 */
class FieldSetRegistry {

    private Map<String, FieldSet> fieldSets = new HashMap<String, FieldSet>()

    public void addFieldSet(FieldSet fs){
        if(fs != null)
            fieldSets.put(fs.getName(), fs)
    }

    public FieldSet getFieldSetByName(String fieldSetName){
        return fieldSets.get(fieldSetName)
    }

    public FieldSet getFieldSetByIdentifier(String identifier){

        if(fieldSets.size() > 0){
            //check each field set in the registry
            for(FieldSet fs : fieldSets.values()){
                //get the identifier field for each field
                IdentifierField f = fs.getIdentifierField()
                if(f != null){
                    //found the identifier, then check if it equals the identifier read in. Here the identifier is case insensitive
                    if(f.getValue().equalsIgnoreCase(identifier)){
                        //found the matched identifier, return the field set
                        return fs
                    }
                }
            }
        }

        //could not find a match, return null
        return null
    }

    public int size(){
        return fieldSets.size()
    }

    public FieldSet getUniqueOne(){
        if(fieldSets.size() == 1){
            return fieldSets.values().asList().get(0)
        }
        else{
            return null
        }
    }

}