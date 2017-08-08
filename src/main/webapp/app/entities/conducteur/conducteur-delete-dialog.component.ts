import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Conducteur } from './conducteur.model';
import { ConducteurPopupService } from './conducteur-popup.service';
import { ConducteurService } from './conducteur.service';

@Component({
    selector: 'jhi-conducteur-delete-dialog',
    templateUrl: './conducteur-delete-dialog.component.html'
})
export class ConducteurDeleteDialogComponent {

    conducteur: Conducteur;

    constructor(
        private conducteurService: ConducteurService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.conducteurService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'conducteurListModification',
                content: 'Deleted an conducteur'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-conducteur-delete-popup',
    template: ''
})
export class ConducteurDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private conducteurPopupService: ConducteurPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.conducteurPopupService
                .open(ConducteurDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
