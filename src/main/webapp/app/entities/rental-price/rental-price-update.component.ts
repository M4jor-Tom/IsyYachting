import { Component, Vue, Inject } from 'vue-property-decorator';

import { decimal, required } from 'vuelidate/lib/validators';

import AlertService from '@/shared/alert/alert.service';

import RentalService from '@/entities/rental/rental.service';
import { IRental } from '@/shared/model/rental.model';

import { IRentalPrice, RentalPrice } from '@/shared/model/rental-price.model';
import RentalPriceService from './rental-price.service';
import { Season } from '@/shared/model/enumerations/season.model';

const validations: any = {
  rentalPrice: {
    value: {
      required,
      decimal,
    },
    season: {
      required,
    },
    rental: {
      required,
    },
  },
};

@Component({
  validations,
})
export default class RentalPriceUpdate extends Vue {
  @Inject('rentalPriceService') private rentalPriceService: () => RentalPriceService;
  @Inject('alertService') private alertService: () => AlertService;

  public rentalPrice: IRentalPrice = new RentalPrice();

  @Inject('rentalService') private rentalService: () => RentalService;

  public rentals: IRental[] = [];
  public seasonValues: string[] = Object.keys(Season);
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.rentalPriceId) {
        vm.retrieveRentalPrice(to.params.rentalPriceId);
      }
      vm.initRelationships();
    });
  }

  created(): void {
    this.currentLanguage = this.$store.getters.currentLanguage;
    this.$store.watch(
      () => this.$store.getters.currentLanguage,
      () => {
        this.currentLanguage = this.$store.getters.currentLanguage;
      }
    );
  }

  public save(): void {
    this.isSaving = true;
    if (this.rentalPrice.id) {
      this.rentalPriceService()
        .update(this.rentalPrice)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('isyYachtingApp.rentalPrice.updated', { param: param.id });
          return this.$root.$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Info',
            variant: 'info',
            solid: true,
            autoHideDelay: 5000,
          });
        })
        .catch(error => {
          this.isSaving = false;
          this.alertService().showHttpError(this, error.response);
        });
    } else {
      this.rentalPriceService()
        .create(this.rentalPrice)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('isyYachtingApp.rentalPrice.created', { param: param.id });
          this.$root.$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Success',
            variant: 'success',
            solid: true,
            autoHideDelay: 5000,
          });
        })
        .catch(error => {
          this.isSaving = false;
          this.alertService().showHttpError(this, error.response);
        });
    }
  }

  public retrieveRentalPrice(rentalPriceId): void {
    this.rentalPriceService()
      .find(rentalPriceId)
      .then(res => {
        this.rentalPrice = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.rentalService()
      .retrieve()
      .then(res => {
        this.rentals = res.data;
      });
  }
}
