package org.usfirst.frc.team3164.lib.vision;

import java.util.Comparator;
import java.util.Vector;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.ImageType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ToteParser {
	public static class ToteParseResult {
		public boolean isTote;
		public Image parsedImage;
	}
	private static class ParticleReport implements Comparator<ParticleReport>, Comparable<ParticleReport> {
		double PercentAreaToImageArea;
		double Area;
		double BoundingRectLeft;
		double BoundingRectTop;
		double BoundingRectRight;
		double BoundingRectBottom;

		public int compareTo(ParticleReport r) {
			return (int) (r.Area - this.Area);
		}

		public int compare(ParticleReport r1, ParticleReport r2) {
			return (int) (r1.Area - r2.Area);
		}
}	;
	private static class Scores {
		double Area;
		double Aspect;
	};
	public static ToteParseResult parseImg(Image frame) {
		Image binaryFrame;
		int imaqError;
		ToteParseResult results = new ToteParseResult();
		NIVision.Range TOTE_HUE_RANGE = new NIVision.Range(130, 180); // Default hue //100,140
		// range for
		// yellow
		// tote
		NIVision.Range TOTE_SAT_RANGE = new NIVision.Range(90, 255); // Default //20,225
		// saturation
		// range for
		// yellow
		// tote
		NIVision.Range TOTE_VAL_RANGE = new NIVision.Range(90, 300); // Default //100,300
		// value
		// range for
		// yellow
		// tote
		double AREA_MINIMUM = 0.5; // Default Area minimum for particle as a
		// percentage of total image area
		double LONG_RATIO = 2.22; // Tote long side = 26.9 / Tote height = 12.1 =
		// 2.22
		double SHORT_RATIO = 1.4; // Tote short side = 16.9 / Tote height = 12.1 =
		// 1.4
		double SCORE_MIN = 50.0; // Minimum score to be considered a tote
		// default, 64 for m1013, 51.7 for 206, 52 for
		// HD3000 square, 60 for HD3000 640x480
		NIVision.ParticleFilterCriteria2 criteria[] = new NIVision.ParticleFilterCriteria2[1];
		NIVision.ParticleFilterOptions2 filterOptions = new NIVision.ParticleFilterOptions2(
				0, 0, 1, 1);
		Scores scores = new Scores();
		TOTE_HUE_RANGE.minValue = (int) SmartDashboard.getNumber(
				"Tote hue min", TOTE_HUE_RANGE.minValue);
		TOTE_HUE_RANGE.maxValue = (int) SmartDashboard.getNumber(
				"Tote hue max", TOTE_HUE_RANGE.maxValue);
		TOTE_SAT_RANGE.minValue = (int) SmartDashboard.getNumber(
				"Tote sat min", TOTE_SAT_RANGE.minValue);
		TOTE_SAT_RANGE.maxValue = (int) SmartDashboard.getNumber(
				"Tote sat max", TOTE_SAT_RANGE.maxValue);
		TOTE_VAL_RANGE.minValue = (int) SmartDashboard.getNumber(
				"Tote val min", TOTE_VAL_RANGE.minValue);
		TOTE_VAL_RANGE.maxValue = (int) SmartDashboard.getNumber(
				"Tote val max", TOTE_VAL_RANGE.maxValue);
		
		binaryFrame = NIVision.imaqCreateImage(ImageType.IMAGE_U8, 0);
		criteria[0] = new NIVision.ParticleFilterCriteria2(
				NIVision.MeasurementType.MT_AREA_BY_IMAGE_AREA, AREA_MINIMUM,
				100.0, 0, 0);
		
		// Threshold the image looking for yellow (tote color)
		NIVision.imaqColorThreshold(binaryFrame, frame, 255,
				NIVision.ColorMode.HSV, TOTE_HUE_RANGE, TOTE_SAT_RANGE,
				TOTE_VAL_RANGE);

		// Send particle count to dashboard
		int numParticles = NIVision.imaqCountParticles(binaryFrame, 1);
		SmartDashboard.putNumber("Masked particles", numParticles);

		results.parsedImage = binaryFrame;

		// filter out small particles
		float areaMin = (float) SmartDashboard.getNumber("Area min %",
				AREA_MINIMUM);
		criteria[0].lower = areaMin;
		imaqError = NIVision.imaqParticleFilter4(binaryFrame, binaryFrame,
				criteria, filterOptions, null);

		// Send particle count after filtering to dashboard
		numParticles = NIVision.imaqCountParticles(binaryFrame, 1);
		SmartDashboard.putNumber("Filtered particles", numParticles);

		if (numParticles > 0) {
			// Measure particles and sort by particle size
			Vector<ParticleReport> particles = new Vector<ParticleReport>();
			for (int particleIndex = 0; particleIndex < numParticles; particleIndex++) {
				ParticleReport par = new ParticleReport();
				par.PercentAreaToImageArea = NIVision.imaqMeasureParticle(
						binaryFrame, particleIndex, 0,
						NIVision.MeasurementType.MT_AREA_BY_IMAGE_AREA);
				par.Area = NIVision.imaqMeasureParticle(binaryFrame,
						particleIndex, 0, NIVision.MeasurementType.MT_AREA);
				par.BoundingRectTop = NIVision.imaqMeasureParticle(
						binaryFrame, particleIndex, 0,
						NIVision.MeasurementType.MT_BOUNDING_RECT_TOP);
				par.BoundingRectLeft = NIVision.imaqMeasureParticle(
						binaryFrame, particleIndex, 0,
						NIVision.MeasurementType.MT_BOUNDING_RECT_LEFT);
				par.BoundingRectBottom = NIVision.imaqMeasureParticle(
						binaryFrame, particleIndex, 0,
						NIVision.MeasurementType.MT_BOUNDING_RECT_BOTTOM);
				par.BoundingRectRight = NIVision.imaqMeasureParticle(
						binaryFrame, particleIndex, 0,
						NIVision.MeasurementType.MT_BOUNDING_RECT_RIGHT);
				particles.add(par);
			}
			particles.remove(null);

			// This example only scores the largest particle. Extending to
			// score all particles and choosing the desired one is left as
			// an exercise
			// for the reader. Note that this scores and reports information
			// about a single particle (single L shaped target). To get
			// accurate information
			// about the location of the tote (not just the distance) you
			// will need to correlate two adjacent targets in order to find
			// the true center of the tote.
			scores.Aspect = AspectScore(particles.elementAt(0));
			SmartDashboard.putNumber("Aspect", scores.Aspect);
			scores.Area = AreaScore(particles.elementAt(0));
			SmartDashboard.putNumber("Area", scores.Area);
			boolean isTote = scores.Aspect > SCORE_MIN
					&& scores.Area > SCORE_MIN;
					results.isTote = isTote;
		} else {
			results.isTote = false;
		}
		return results;
	}
		
		@SuppressWarnings("unused")
		private static boolean CompareParticleSizes(ParticleReport particle1,
				ParticleReport particle2) {
			// we want descending sort order
			return particle1.PercentAreaToImageArea > particle2.PercentAreaToImageArea;
		}

		/**
		 * Converts a ratio with ideal value of 1 to a score. The resulting function
		 * is piecewise linear going from (0,0) to (1,100) to (2,0) and is 0 for all
		 * inputs outside the range 0-2
		 */
		private static double ratioToScore(double ratio) {
			return (Math.max(0, Math.min(100 * (1 - Math.abs(1 - ratio)), 100)));
		}

		private static double AreaScore(ParticleReport report) {
			double boundingArea = (report.BoundingRectBottom - report.BoundingRectTop)
					* (report.BoundingRectRight - report.BoundingRectLeft);
			// Tape is 7" edge so 49" bounding rect. With 2" wide tape it covers 24"
			// of the rect.
			return ratioToScore((49 / 24) * report.Area / boundingArea);
		}

		/**
		 * Method to score if the aspect ratio of the particle appears to match the
		 * retro-reflective target. Target is 7"x7" so aspect should be 1
		 */
		private static double AspectScore(ParticleReport report) {
			return ratioToScore(((report.BoundingRectRight - report.BoundingRectLeft) / (report.BoundingRectBottom - report.BoundingRectTop)));
		}

		/**
		 * Computes the estimated distance to a target using the width of the
		 * particle in the image. For more information and graphics showing the math
		 * behind this approach see the Vision Processing section of the
		 * ScreenStepsLive documentation.
		 * 
		 * @param image
		 *            The image to use for measuring the particle estimated
		 *            rectangle
		 * @param report
		 *            The Particle Analysis Report for the particle
		 * @param isLong
		 *            Boolean indicating if the target is believed to be the long
		 *            side of a tote
		 * @return The estimated distance to the target in feet.
		 */
		private static double computeDistance(Image image, ParticleReport report) {
			double VIEW_ANGLE = 60;
			double normalizedWidth, targetWidth;
			NIVision.GetImageSizeResult size;

			size = NIVision.imaqGetImageSize(image);
			normalizedWidth = 2
					* (report.BoundingRectRight - report.BoundingRectLeft)
					/ size.width;
			targetWidth = 7;

			return targetWidth
					/ (normalizedWidth * 12 * Math.tan(VIEW_ANGLE * Math.PI
							/ (180 * 2)));
		}
		
}
