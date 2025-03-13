package parser.dataTypes;

import java.util.ArrayList;
import java.util.List;

public class DefinitionArguments {
	
	private List<DefinitionVariableType> defVarTypes = new ArrayList<>();
	
	public void addArg(DefinitionVariableType defVarType) {
		defVarTypes.add(defVarType);
	}
	
}
