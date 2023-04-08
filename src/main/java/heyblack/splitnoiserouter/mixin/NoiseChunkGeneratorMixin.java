package heyblack.splitnoiserouter.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructureSet;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntryList;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.biome.source.util.VanillaTerrainParameters;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.NoiseChunkGenerator;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import net.minecraft.world.gen.noise.NoiseRouter;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;

@Environment(EnvType.CLIENT)
@Mixin(NoiseChunkGenerator.class)
public abstract class NoiseChunkGeneratorMixin extends ChunkGenerator
{

    @Shadow @Final private NoiseRouter noiseRouter;

    public NoiseChunkGeneratorMixin(Registry<StructureSet> registry, Optional<RegistryEntryList<StructureSet>> optional, BiomeSource biomeSource)
    {
        super(registry, optional, biomeSource);
    }

    @Inject(method = "getDebugHudText", at = @At("HEAD"), cancellable = true)
    public void showMeMyCrossHair(List<String> text, BlockPos pos, CallbackInfo ci)
    {
        DecimalFormat decimalFormat = new DecimalFormat("0.000");
        DensityFunction.UnblendedNoisePos unblendedNoisePos = new DensityFunction.UnblendedNoisePos(pos.getX(), pos.getY(), pos.getZ());
        double d = this.noiseRouter.ridges().sample(unblendedNoisePos);
        text.add("NoiseRouter T: " + decimalFormat.format(this.noiseRouter.temperature().sample(unblendedNoisePos)) + " H: " + decimalFormat.format(this.noiseRouter.humidity().sample(unblendedNoisePos)) + " C: " + decimalFormat.format(this.noiseRouter.continents().sample(unblendedNoisePos)) + " E: " + decimalFormat.format(this.noiseRouter.erosion().sample(unblendedNoisePos)) + " D: " + decimalFormat.format(this.noiseRouter.depth().sample(unblendedNoisePos)));
        text.add("NoiseRouter W: " + decimalFormat.format(d) + " PV: " + decimalFormat.format(VanillaTerrainParameters.getNormalizedWeirdness((float)d)) + " AS: " + decimalFormat.format(this.noiseRouter.initialDensityWithoutJaggedness().sample(unblendedNoisePos)) + " N: " + decimalFormat.format(this.noiseRouter.finalDensity().sample(unblendedNoisePos)));
        ci.cancel();
    }
}
