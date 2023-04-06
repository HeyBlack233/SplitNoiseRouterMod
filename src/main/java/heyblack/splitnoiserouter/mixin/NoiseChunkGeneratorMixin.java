package heyblack.splitnoiserouter.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.chunk.NoiseChunkGenerator;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import net.minecraft.world.gen.densityfunction.DensityFunctions;
import net.minecraft.world.gen.noise.NoiseConfig;
import net.minecraft.world.gen.noise.NoiseRouter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.text.DecimalFormat;
import java.util.List;

@Environment(EnvType.CLIENT)
@Mixin(NoiseChunkGenerator.class)
public abstract class NoiseChunkGeneratorMixin
{
    @Inject(method = "getDebugHudText", at = @At("HEAD"), cancellable = true)
    public void showMeMyCrossHair(List<String> text, NoiseConfig noiseConfig, BlockPos pos, CallbackInfo ci)
    {
        DecimalFormat decimalFormat = new DecimalFormat("0.000");
        NoiseRouter noiseRouter = noiseConfig.getNoiseRouter();
        DensityFunction.UnblendedNoisePos unblendedNoisePos = new DensityFunction.UnblendedNoisePos(pos.getX(), pos.getY(), pos.getZ());
        double d = noiseRouter.ridges().sample(unblendedNoisePos);
        text.add("NoiseRouter T: " + decimalFormat.format(noiseRouter.temperature().sample(unblendedNoisePos)) + " V: " + decimalFormat.format(noiseRouter.vegetation().sample(unblendedNoisePos)) + " C: " + decimalFormat.format(noiseRouter.continents().sample(unblendedNoisePos)) + " E: " + decimalFormat.format(noiseRouter.erosion().sample(unblendedNoisePos)) + " D: " + decimalFormat.format(noiseRouter.depth().sample(unblendedNoisePos)));
        text.add("NoiseRouter W: " + decimalFormat.format(d) + " PV: " + decimalFormat.format(DensityFunctions.getPeaksValleysNoise((float)d)) + " AS: " + decimalFormat.format(noiseRouter.initialDensityWithoutJaggedness().sample(unblendedNoisePos)) + " N: " + decimalFormat.format(noiseRouter.finalDensity().sample(unblendedNoisePos)));
        ci.cancel();
    }
}
