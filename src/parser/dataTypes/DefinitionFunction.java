package parser.dataTypes;

public class DefinitionFunction extends Blocks {

	private String functionName;
	private DefinitionArguments defArgs;
	private Block block;
	
	public DefinitionFunction(String name, DefinitionArguments defArgs, Block block) {
		functionName = name;
		this.defArgs = defArgs;
		this.block = block;
	}
	
	@Override
	public void excecute() {
	}

}
