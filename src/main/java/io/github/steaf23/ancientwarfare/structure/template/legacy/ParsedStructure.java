package io.github.steaf23.ancientwarfare.structure.template.legacy;

import io.github.steaf23.ancientwarfare.structure.template.StructureVersion;
import net.minecraft.core.Vec3i;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class ParsedStructure {
	/*
	 * base datas
	 */
	public final String name;
	public final Vec3i size;
	public final Vec3i offset;
	private StructureVersion version;

	/*
	 * stored template data
	 */
	public final Set<String> modDependencies;
	private Map<Integer, TemplateRuleBlock> blockRules;
	private Map<Integer, TemplateRuleEntityBase> entityRules;
	private short[] templateData;
	private List<BuildResource> resourceList;

	/*
	 * world generation placement validation settings
	 */
//	private StructureValidator validator = StructureValidationType.GROUND.getValidator();
	private StructureValidator validator = new StructureValidator();

	public ParsedStructure(String name, Set<String> modDependencies, Vec3i size, Vec3i offset) {
		this(name, modDependencies, StructureVersion.NONE, size, offset);
	}

	public ParsedStructure(String name, Set<String> modDependencies, StructureVersion version, Vec3i size, Vec3i offset) {
		this.modDependencies = modDependencies;
		this.version = version;
		this.name = name;
		this.size = size;
		this.offset = offset;
	}

	public Map<Integer, TemplateRuleEntityBase> getEntityRules() {
		return entityRules;
	}

	public Map<Integer, TemplateRuleBlock> getBlockRules() {
		return blockRules;
	}

	public short[] getTemplateData() {
		return templateData;
	}

	public StructureValidator getValidationSettings() {
		return validator;
	}

	public void setBlockRules(Map<Integer, TemplateRuleBlock> rules) {
		this.blockRules = rules;
	}

	public void setEntityRules(Map<Integer, TemplateRuleEntityBase> rules) {
		this.entityRules = rules;
	}

	public void setTemplateData(short[] datas) {
		this.templateData = datas;
	}

	public void setValidationSettings(StructureValidator settings) {
		this.validator = settings;
	}

	public Optional<TemplateRuleBlock> getRuleAt(Vec3i pos) {
		int index = getIndex(pos, size);
		int ruleIndex = index >= 0 && index < templateData.length ? templateData[index] : -1;
		return Optional.ofNullable(blockRules.get(ruleIndex));
	}

	public static int getIndex(Vec3i pos, Vec3i size) {
		return (pos.getY() * size.getX() * size.getZ()) + (pos.getZ() * size.getX()) + pos.getX();
	}

	@Override
	public String toString() {
		return "name: " + name + "\n" + "size: " + size.getX() + ", " + size.getY() + ", " + size.getZ() + "\n" + "buildKey: " + offset.getX() + ", " + offset.getY() + ", " + offset.getZ();
	}

	public List<BuildResource> getResourceList() {
//		if (resourceList == null) {
//			List<BuildResource> allResources = new ArrayList<>();
//
//			NonNullList<ItemStack> consumeOnlyResources = NonNullList.create();
//
//			MathUtils.getAllVecsInBox(Vec3i.NULL_VECTOR, new Vec3i(size.getX() - 1, size.getY() - 1, size.getZ() - 1))
//					.forEach(pos -> getRuleAt(pos).ifPresent(r -> {
//								ItemStack remainingStack = r.getRemainingStack();
//								List<ItemStack> resources = r.getResources();
//								if (remainingStack.isEmpty() || resources.size() > 1) {
//									consumeOnlyResources.addAll(resources);
//								} else {
//									resources.forEach(res -> allResources.add(new BuildResource(res, remainingStack)));
//								}
//							})
//					);
//			InventoryTools.compactStackList(consumeOnlyResources).forEach(res -> allResources.add(new BuildResource(res)));
//
//			resourceList = allResources;
//		}
//		return resourceList;
		return List.of();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof ParsedStructure))
			return false;
		ParsedStructure that = (ParsedStructure) o;
		return size.equals(that.size) && offset.equals(that.offset) && name.equals(that.name);
	}

	@Override
	public int hashCode() {
		int result = size.hashCode();
		result = 31 * result + offset.hashCode();
		result = 31 * result + name.hashCode();
		return result;
	}

	public StructureVersion getVersion() {
		return version;
	}

	public Vec3i getSize() {
		return size;
	}

	public Vec3i getOffset() {
		return offset;
	}

	public static class BuildResource {
		private static final String STACK_TO_RETURN_TAG = "stackToReturn";
		private ItemStack stackRequired;
		private ItemStack stackToReturn = ItemStack.EMPTY;
		private int requiredOriginalCount = 0;

		private BuildResource(ItemStack stackRequired, ItemStack stackToReturn) {
			this.stackRequired = stackRequired;
			this.stackToReturn = stackToReturn;
			requiredOriginalCount = stackRequired.getCount();
		}

		private BuildResource(ItemStack stackRequired) {
			this.stackRequired = stackRequired;
		}

		public BuildResource() {}

		public ItemStack getStackRequired() {
			return stackRequired;
		}

		public BuildResource copy() {
			return new BuildResource(stackRequired.copy(), stackToReturn.copy());
		}

		public ItemStack shrinkStackRequiredAndGetRemaining() {
			stackRequired.shrink(1);
			if (!stackToReturn.isEmpty() && stackRequired.getCount() % requiredOriginalCount == 0) {
				return stackToReturn.copy();
			}
			return ItemStack.EMPTY;
		}

		public boolean isEmpty() {
			return stackRequired.isEmpty();
		}
	}
}