package arathain.vigorem.anim;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class EntityQuery {
	public float anim_time;
	public float time_of_day;
	public int moon_phase;
	public boolean is_on_ground;
	public boolean is_on_fire;
	public boolean is_in_water;
	public boolean is_in_water_or_rain;
	public float health;
	public float yaw_speed;
	public float pitch;
	public float ground_speed;
	public float max_health;

	public void update(Entity e, float animtime, World w) {
		updateTime(animtime);
		time_of_day = w.getTimeOfDay();
		moon_phase = w.getMoonPhase();
		pitch = e.getPitch();
		health = 0;
		max_health = 0;
		yaw_speed = e.getYaw()-e.prevYaw;
		ground_speed = (float) e.getVelocity().length()*20f;
		is_on_ground = e.isOnGround();
		is_on_fire = e.isOnFire();
		is_in_water_or_rain = e.isTouchingWaterOrRain();
		is_in_water = e.isTouchingWater();
	}
	public void update(LivingEntity e, float animtime, World w) {
		update((Entity) e, animtime, w);
		health = e.getHealth();
		max_health = e.getMaxHealth();
	}
	public void updateTime(float animtime) {
		anim_time = animtime/20f;
	}
}
