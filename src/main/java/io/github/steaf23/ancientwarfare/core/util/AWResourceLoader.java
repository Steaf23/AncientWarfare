package io.github.steaf23.ancientwarfare.core.util;

import com.mojang.serialization.Codec;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;

public class AWResourceLoader<T> extends SimpleJsonResourceReloadListener<T> {

	private Map<Identifier, T> entries;

	public AWResourceLoader(Codec<T> codec, String prefix) {
		super(codec, FileToIdConverter.json(prefix));
	}

	public Map<Identifier, T> entries() {
		return entries;
	}

	public Collection<T> allValues() {
		return entries.values();
	}

	public @Nullable T byId(Identifier id) {
		return entries.get(id);
	}

	@Override
	protected void apply(Map<Identifier, T> preparations, ResourceManager manager, ProfilerFiller profiler) {
		entries = preparations;
	}
}
